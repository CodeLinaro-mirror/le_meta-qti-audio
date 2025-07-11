inherit linux-kernel-base deploy
 
SUMMARY = "Audio Drivers Kernel Modules for AudioReach"
DESCRIPTION = "This is the AudioReach based audio driver based on ASoC architecture, used to communicate with DSP."
 
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"
DEPENDS += "virtual/kernel"
SRCREV = "${AUTOREV}"
PR = "r0"
 
INSANE_SKIP:${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"
 
FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "file://vendor/qcom/opensource/audio-kernel/"
SRC_URI:append = " file://${BASEMACHINE}/audio_load.conf"
 
S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"
 
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"
INTERMEDIAT_KERNEL_PATH = "${WORKDIR}/out/${KERNEL_DEFCONFIG}"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${INTERMEDIAT_KERNEL_PATH}"
 
do_configure() {
    find . -name "*.cmd" -exec rm -rf {} \;
    cp -f ${WORKSPACE}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
}
 
do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"
do_compile:qcm2290-mtp() {
    cd ${KERNEL_PLATFORM_PATH}
 
    KBUILD_OPTIONS+="TARGET_SUPPORT=${BASEMACHINE}" \
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    MODULE_OUT=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${INTERMEDIAT_KERNEL_PATH} \
    INPLACE_COMPILE=y \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    BOARD_PLATFORM=${BASEMACHINE} \
    ./build/build_module.sh
}

do_compile:qcm4325-mtp() {
    cd ${KERNEL_PLATFORM_PATH}

    KBUILD_OPTIONS+="TARGET_SUPPORT=${BASEMACHINE}" \
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    MODULE_OUT=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${INTERMEDIAT_KERNEL_PATH} \
    INPLACE_COMPILE=y \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    BOARD_PLATFORM=${BASEMACHINE} \
    ./build/build_module.sh
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    for i in $(find ${WORKDIR}/vendor/qcom/opensource/audio-kernel/. -name "*.ko"); do
        install -m 0755 ${i} -D ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    done
}
 
do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0755 ${WORKDIR}/${BASEMACHINE}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
    else
        install -m 0755 ${WORKDIR}/${BASEMACHINE}/audio_load.conf -D ${D}${sysconfdir}/modules/audio_load.conf
    fi
 
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/dsp
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ipc
    rm -fr ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/soc
}

do_module_signing() {
  if [ -f ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ]; then
    for i in ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*
      do
        ${STAGING_KERNEL_DIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ${STAGING_KERNEL_BUILDDIR}/signing_key.x509 ${i}
      done
  elif [ -f ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ]; then
    for i in $(find ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/* -name "*.ko");
      do
   ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
   done
  fi
}
do_deploy() {
    cp -rp ${WORKDIR}/*.ko ${DEPLOYDIR}/
}
 
# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.

RPROVIDES:${PN} += "kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}"
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
RPROVIDES:${PN} += "kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-audio-prm-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-gpr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-spf-machine-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-platform-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-q6-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-snd-event-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-spf-core-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += " kernel-module-stub-dlkm-${KERNEL_VERSION}"
 
 
# install subdirectories under ${sysconfdir}
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
 
KERNEL_CC += "-Wno-error=maybe-uninitialized"
