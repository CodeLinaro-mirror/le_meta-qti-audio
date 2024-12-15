inherit module

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

DESCRIPTION = "QTI Audio drivers"
LICENSE = "GPL-2.0"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PR = "r0"

DEPENDS = "virtual/kernel"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://vendor/qcom/opensource/audio-kernel/"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES:${PN} += "${sysconfdir}/*"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

# Disable parallel make
PARALLEL_MAKE = "-j1"

do_configure() {
  cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
}

do_install:append() {
  install -d ${D}${includedir}/audio-kernel/
  install -d ${D}${includedir}/audio-kernel/linux
  install -d ${D}${includedir}/audio-kernel/linux/mfd
  install -d ${D}${includedir}/audio-kernel/linux/mfd/wcd9xxx
  install -d ${D}${includedir}/audio-kernel/sound
  install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra

  cp -fr ${S}/linux/* ${D}${includedir}/audio-kernel/linux
  install -m 0644 ${S}/sound/* ${D}${includedir}/audio-kernel/sound

  install -m 0755 ${WORKDIR}/${BASEMACHINE}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf

   for i in $(find ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/. -name "*.ko"); do
   mv ${i} ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
   done

   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/asoc
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/dsp
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/ipc
   rm -fr ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/soc
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

addtask do_module_signing after do_package before do_package_write_ipk

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.

RPROVIDES:${PN} += "${@'kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-apr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-bolero-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-csra66x0-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-va-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wsa-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-cpe-lsm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-native-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-pinctrl-lpi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-pinctrl-wcd-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-platform-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-q6-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-q6-pdr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-swr-ctrl-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-swr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-usf-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wglink-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-analog-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-digital-cdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-msm-sdw-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd934x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-hdmi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-ep92-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-ext-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-ext-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-mbhc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-stub-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd-cpe-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd9335-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd9xxx-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wsa881x-analog-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wsa881x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd-spi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-int-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-snd-event-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-rx-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-tx-macro-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd937x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd937x-slave-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-machine-digcdc-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-audio-prm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-gpr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-spf-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-voice-mhi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd938x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd938x-slave-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
