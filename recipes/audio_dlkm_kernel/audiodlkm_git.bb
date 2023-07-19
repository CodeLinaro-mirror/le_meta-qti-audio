SUMMARY = "Audio Drivers Kernel Modules for Elite"
DESCRIPTION = "This is the Elite based audio driver based on ASoC architecture, used to communicate with DSP."

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"
DEPENDS = "virtual/kernel linux-msm-headers"
DEPENDS:append:qrb5165 = " mmdlkm"
PR = "r0"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://vendor/qcom/opensource/audio-kernel/legacy"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel/legacy"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"

inherit linux-kernel-base deploy

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"
FILES:${PN} += "${sysconfdir}/*"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

PARALLEL_MAKE = "-j1"

do_configure() {
  cp -f ${WORKDIR}/vendor/qcom/opensource/audio-kernel/legacy/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/legacy/Makefile
}

do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"
do_compile() {
    echo ${EXTRA_OEMAKE}
    cd ${KERNEL_PLATFORM_PATH}
    KBUILD_OPTIONS+="TARGET_SUPPORT=${BASEMACHINE}" \
    BUILD_CONFIG="msm-kernel/${KERNEL_CONFIG}" \
    EXT_MODULES=${EXT_MODULES} \
    ROOTDIR=${WORKDIR}/ \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    INPLACE_COMPILE=y \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    KBUILD_EXTRA_SYMBOLS=${STAGING_DIR_HOST}/lib/modules/${KERNEL_VERSION}/mm-drivers/Module.symvers \
    ./build/build_module.sh ${EXTRA_OEMAKE}
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    for i in $(find ${WORKDIR}/vendor/qcom/opensource/audio-kernel/legacy/. -name "*.ko"); do
        install -m 0755 ${i} -D ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    done
}

do_install:append() {
    install -d -p ${D}${includedir}/audio-kernel/audio/
    install -d -p ${D}${includedir}/audio-kernel/audio/linux
    install -d -p ${D}${includedir}/audio-kernel/audio/linux/mfd
    install -d -p ${D}${includedir}/audio-kernel/audio/linux/mfd/wcd9xxx
    install -d -p ${D}${includedir}/audio-kernel/audio/sound
    install -d ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio
    install -d ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio/linux
    install -d ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio/linux/mfd
    install -d ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio/linux/mfd/wcd9xxx
    install -d ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio/sound

    cp -fr ${S}/include/uapi/audio/linux/* ${D}${includedir}/audio-kernel/audio/linux
    install -m 0644 ${S}/include/uapi/audio/sound/* ${D}${includedir}/audio-kernel/audio/sound
    cp -fr ${S}/include/uapi/audio/linux/* ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio/linux
    install -m 0644 ${S}/include/uapi/audio/sound/* ${STAGING_KERNEL_BUILDDIR}/audio-kernel/audio/sound

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

do_deploy() {
    cp -rp ${WORKDIR}/*.ko ${DEPLOYDIR}/
}

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
RPROVIDES:${PN} += "${@'kernel-module-wcd938x-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-wcd938x-slave-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-pm2250-spmi-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-rouleur-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-rouleur-slave-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-bt_fm_slim-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
