#inherit module

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

DESCRIPTION = "QTI Audio drivers"
LICENSE = "${@bb.utils.contains('LAYERSERIES_COMPAT_core', 'dunfell',\
           'GPL-2.0','GPL-2.0-only', d)}"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PR = "r0"

DEPENDS = "virtual/kernel linux-msm-headers"
DEPENDS += "${@bb.utils.contains_any('BASEMACHINE', "sa525m sa510m", 'rsync-native', '', d)}"
DEPENDS += "${@bb.utils.contains_any('BASEMACHINE', "sa525m sa510m", 'audiodevicetree', '', d)}"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://vendor/qcom/opensource/audio-kernel/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"

inherit linux-kernel-base deploy

FILES:${PN} += "${@bb.utils.contains('TARGET_BOARD_PLATFORM','sa510m', "${nonarch_base_libdir}/modules/audio/*", "", d)}"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES:${PN} += "${sysconfdir}/*"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

PARALLEL_MAKE = "-j1"

do_configure() {
  cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
}

do_compile:sa525m() {
    oe_runmake clean

    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  && \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    TARGET_SUPPORT=sa525m \
    EXT_MODULES=../../vendor/qcom/opensource/audio-kernel \
    ROOTDIR=${WORKDIR}/ \
    MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/audio-kernel \
    OUT_DIR=${KERNEL_OUT_PATH}/ \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    INSTALL_MODULE_HEADERS=1 \
    ./build/build_module.sh
}

do_compile:sa510m() {
    cd ${KERNEL_PLATFORM_PATH}
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    KBUILD_OPTIONS+="TARGET_SUPPORT=${BASEMACHINE}" \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${KERNEL_OUT_PATH} \
    MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/audio-kernel \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    TARGET_SUPPORT=sa510m \
    ./build/build_module.sh
}

do_install() {
  install -d ${D}${includedir}/audio-kernel/
  install -d ${D}${includedir}/audio-kernel/linux
  install -d ${D}${includedir}/audio-kernel/linux/mfd
  install -d ${D}${includedir}/audio-kernel/linux/mfd/wcd9xxx
  install -d ${D}${includedir}/audio-kernel/sound
  install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

  if [ ${BASEMACHINE} != "sa525m" && ${BASEMACHINE} != "sa510m"];then
    cp -fr ${S}/linux/* ${D}${includedir}/audio-kernel/linux
    install -m 0644 ${S}/sound/* ${D}${includedir}/audio-kernel/sound
    install -m 0755 ${WORKDIR}/${BASEMACHINE}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
    for i in $(find ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/. -name "*.ko"); do
      mv ${i} ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    done
  else
   for i in $(find ${WORKDIR}/vendor/qcom/opensource/audio-kernel/. -name "*.ko"); do
   install -m 0755 ${i} ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
   done
  fi

   cp -fr ${S}/include/ ${STAGING_KERNEL_BUILDDIR}/usr

   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/dsp
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ipc
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/soc
}

do_module_signing() {
  export LD_LIBRARY_PATH="${KERNEL_PREBUILT_DISTDIR}"
  if [ -f ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ]; then
    for i in ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*
      do
        ${STAGING_KERNEL_DIR}/scripts/sign-file sha1 ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ${STAGING_KERNEL_BUILDDIR}/signing_key.x509 ${i}
      done
  elif [ -f ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ]; then
    for i in $(find ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/* -name "*.ko");
      do
   ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha1 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
   bbnote "Signing ${i} module"
   done
  fi
}
do_deploy:sa525m() {
# Deploy unstripped kernel modules into ${DEPLOYDIR}/kernel_modules for debugging purposes
    install -d ${DEPLOYDIR}/kernel_modules
    for kmod in $(find ${D} -name "*.ko") ; do
        install -m 0644 $kmod ${DEPLOYDIR}/kernel_modules
    done
}

do_deploy:sa510m() {
# Deploy unstripped kernel modules into ${DEPLOYDIR}/kernel_modules for debugging purposes
    install -d ${DEPLOYDIR}/kernel_modules
    for kmod in $(find ${D} -name "*.ko") ; do
        install -m 0644 $kmod ${DEPLOYDIR}/kernel_modules
    done
}

addtask do_module_signing after do_package before do_package_write_ipk

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.

RPROVIDES:${PN} += "kernel-module-spf-core-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-audio-prm-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-gpr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-cdc-pin-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-apr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-bolero-cdc-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-csra66x0-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-va-macro-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wsa-macro-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-cpe-lsm-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-machine-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-native-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-pinctrl-lpi-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-pinctrl-wcd-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-platform-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-q6-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-q6-pdr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-swr-ctrl-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-swr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-usf-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wglink-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-analog-cdc-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-digital-cdc-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-msm-sdw-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd934x-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-hdmi-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-ep92-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-machine-ext-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-machine-ext-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-mbhc-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-stub-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd-core-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd-cpe-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd9335-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd9xxx-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wsa881x-analog-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wsa881x-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd-spi-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-machine-int-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-snd-event-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-rx-macro-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-tx-macro-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd937x-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd937x-slave-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-machine-digcdc-dlkm-${KERNEL_VERSION}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
