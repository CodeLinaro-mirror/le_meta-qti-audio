inherit module pkgconfig
inherit linux-kernel-base deploy

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
KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

EXTRA_OEMAKE += "M="${S}" \
		 TARGET_SUPPORT=${BASEMACHINE}"

FILES:${PN} += "${nonarch_base_libdir}/modules/audio/*"

do_configure() {
    cp -fr ${S}/Makefile.am ${S}/Makefile
}

do_install:sdxpinn() {
     install -d ${D}${includedir}/audio-kernel/
     install -d ${D}${nonarch_base_libdir}/modules/audio/asoc
     install -d ${D}${nonarch_base_libdir}/modules/audio/soc
     install -d ${D}${nonarch_base_libdir}/modules/audio/dsp
     install -d ${D}${nonarch_base_libdir}/modules/audio/ipc
     install -d ${D}${nonarch_base_libdir}/modules/audio/asoc/codecs
     install -d ${D}${nonarch_base_libdir}/modules/audio/asoc/codecs/wcd934x

     cp -fr ${S}/include/* ${STAGING_KERNEL_BUILDDIR}/usr/
     install -m 0755 ${S}/asoc/*.ko ${D}/${nonarch_base_libdir}/modules/audio/asoc/
     install -m 0755 ${S}/soc/*.ko ${D}/${nonarch_base_libdir}/modules/audio/soc/
     install -m 0755 ${S}/dsp/*.ko ${D}/${nonarch_base_libdir}/modules/audio/dsp/
     install -m 0755 ${S}/ipc/*.ko ${D}/${nonarch_base_libdir}/modules/audio/ipc/
     install -m 0755 ${S}/asoc/codecs/*.ko ${D}/${nonarch_base_libdir}/modules/audio/asoc/codecs/
     install -m 0755 ${S}/asoc/codecs/wcd934x/*.ko ${D}/${nonarch_base_libdir}/modules/audio/asoc/codecs/wcd934x/
}

do_module_signing() {
  export LD_LIBRARY_PATH="${KERNEL_PREBUILT_DISTDIR}"
  if [ -f ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ]; then
    for i in ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*
      do
        ${STAGING_KERNEL_DIR}/scripts/sign-file sha1 ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ${STAGING_KERNEL_BUILDDIR}/signing_key.x509 ${i}
      done
  elif [ -f ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ]; then
    if [ "${BASEMACHINE}" == "sdxpinn" ]; then
      for i in $(find ${PKGDEST}/${PN}${nonarch_base_libdir}/modules/audio/* -name "*.ko");
        do
          ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha1 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
          bbnote "Signing ${i} module"
        done
    else
      for i in $(find ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/* -name "*.ko");
        do
          ${STAGING_KERNEL_BUILDDIR}/scripts/sign-file sha1 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
          bbnote "2.Signing ${i} module"
        done
    fi
  fi
}

addtask do_module_signing after do_package before do_package_write_ipk

# The inherit of module.bbclass will automatically name module packages with$
# kernel-module-" prefix as required by the oe-core build environment. Also it$
# replaces '_' with '-' in the module name.$
RPROVIDES:${PN} += "kernel-module-spf-core-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-audio-prm-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-gpr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-apr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-native-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-machine-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-pinctrl-wcd-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-platform-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-q6-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-q6-pdr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-stub-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-snd-event-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd-core-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-mbhc-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd9xxx-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wcd934x-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-wsa881x-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-swr-dlkm-${KERNEL_VERSION}"
RPROVIDES:${PN} += "kernel-module-swr-ctrl-dlkm-${KERNEL_VERSION}"
