inherit linux-kernel-base deploy

SUMMARY = "Audio Drivers Kernel Modules for AudioReach"
DESCRIPTION = "This is the AudioReach based audio driver based on ASoC architecture, used to communicate with DSP."

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

SRCREV = "${AUTOREV}"
PR = "r0"

INSANE_SKIP:${PN} = "ldflags"
INHIBIT_PACKAGE_DEBUG_SPLIT = "1"
INHIBIT_PACKAGE_STRIP = "1"

#####Add for DDK
DDK_BUILD ?= "false"
DEPENDS += "${@bb.utils.contains('DDK_BUILD', 'false', \
    'virtual/kernel mmdlkm', '', d)}"
OVERRIDES:append = "${@':ddk_build' if d.getVar('DDK_BUILD') == 'true' else ''}"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "file://vendor/qcom/opensource/audio-kernel/"
SRC_URI:append = " file://${BASEMACHINE}/audio_load.conf"
SRC_URI:append = " file://display/vendor/qcom/opensource/mm-drivers/msm_ext_display/include"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"
INTERMEDIAT_KERNEL_PATH = "${WORKDIR}/out/${KERNEL_DEFCONFIG}"
INTERMEDIATE_KERNEL_PATH = "${WORKDIR}/out/"
EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${INTERMEDIAT_KERNEL_PATH}"

do_configure() {
    find . -name "*.cmd" -exec rm -rf {} \;
    cp -f ${WORKSPACE}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
}

do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"
do_compile() {
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
    KBUILD_EXTRA_SYMBOLS=${STAGING_DIR_HOST}/usr/lib/modules/${KERNEL_VERSION}/mm-drivers/Module.symvers \
    ./build/build_module.sh
}

do_compile:ddk_build() {
    cd ${KERNEL_PLATFORM_PATH}
    ENABLE_DDK_BUILD=${DDK_BUILD} \
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
    BUILD_CONFIG=soc-repo/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${INTERMEDIAT_KERNEL_PATH} \
    MODULE_OUT=${S} \
    ./build/build_module.sh
}

do_install() {
    install -d ${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    for i in $(find ${WORKDIR}/vendor/qcom/opensource/audio-kernel/. -name "*.ko"); do
        install -m 0755 ${i} -D ${D}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/
    done
    install ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Module.symvers -D ${D}${base_libdir}/modules/${KERNEL_VERSION}/extra/Module.symvers
}

do_install:ddk_build() {
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

do_deploy() {
    cp -rp ${WORKDIR}/*.ko ${DEPLOYDIR}/
}

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.
RPROVIDES:${PN} += "${@'kernel-module-adsp-loader-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@'kernel-module-audio-pkt-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-audio-prm-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-audpkt-ion-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-gpr-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-spf-machine-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-platform-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-q6-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-q6-notifier-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-snd-event-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-spf-core-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"
RPROVIDES:${PN} += "${@' kernel-module-stub-dlkm-${KERNEL_VERSION}'.replace('_', '-')}"


# install subdirectories under ${sysconfdir}
FILES:${PN} += "${sysconfdir}/*"
FILES:${PN} += "${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*"

KERNEL_CC += "-Wno-error=maybe-uninitialized"
