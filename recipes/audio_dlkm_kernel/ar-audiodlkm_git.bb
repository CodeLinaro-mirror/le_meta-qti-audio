inherit linux-kernel-base deploy

# if is TARGET_KERNEL_ARCH is set inherit qtikernel-arch to compile for that arch.
inherit ${@bb.utils.contains('TARGET_KERNEL_ARCH', 'aarch64', 'qtikernel-arch', '', d)}

SUMMARY = "Audio Drivers Kernel Modules for AudioReach"
DESCRIPTION = "This is the AudioReach based audio driver based on ASoC architecture, used to communicate with DSP."
LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"

PR = "r0"

DEPENDS = "virtual/kernel"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI  = "file://vendor/qcom/opensource/audio-kernel/"
SRC_URI += "file://${BASEMACHINE}/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"
INTERMEDIATE_KERNEL_PATH = "${WORKDIR}/out/${KERNEL_DEFCONFIG}"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

FILES:${PN} += "${nonarch_base_libdir}/modules"
FILES:${PN} += "${sysconfdir}"

do_configure[depends] += "virtual/kernel:do_shared_workdir"
do_configure() {
    cp -f ${WORKSPACE}/vendor/qcom/opensource/audio-kernel/Makefile.am ${WORKDIR}/vendor/qcom/opensource/audio-kernel/Makefile
}

do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"
do_compile[network]    = "1"
do_compile() {
    cd ${KERNEL_PLATFORM_PATH}

    ENABLE_DDK_BUILD=${DDK_BUILD} \
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    VARIANT=${KERNEL_DEFCONFIG_VARIANT} \
    BUILD_CONFIG=soc-repo/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${INTERMEDIATE_KERNEL_PATH} \
    MODULE_OUT=${S} \
    ./build/build_module.sh
}

do_install() {
    MODDIR=${D}${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra
    install -d ${MODDIR}

    for i in $(find ${S} ${INTERMEDIATE_KERNEL_PATH} -name "*.ko" 2>/dev/null); do
        install -m 0644 ${i} ${MODDIR}/
    done

    if [ -f ${S}/Module.symvers ]; then
        install -m 0644 ${S}/Module.symvers ${MODDIR}/Module.symvers
    fi

    if ${@bb.utils.contains('DISTRO_FEATURES', 'systemd', 'true', 'false', d)}; then
        install -m 0644 ${WORKDIR}/${BASEMACHINE}/audio_load.conf -D ${D}${sysconfdir}/modules-load.d/audio_load.conf
    else
        install -m 0644 ${WORKDIR}/${BASEMACHINE}/audio_load.conf -D ${D}${sysconfdir}/modules/audio_load.conf
    fi
}

do_module_signing() {
    if [ -f ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ]; then
        for i in ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra/*; do
            ${STAGING_KERNEL_DIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/signing_key.priv ${STAGING_KERNEL_BUILDDIR}/signing_key.x509 ${i}
        done
    elif [ -f ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ]; then
        for i in $(find ${PKGDEST}/${PN}/${nonarch_base_libdir}/modules/${KERNEL_VERSION}/extra -name "*.ko"); do
            ${STAGING_KERNEL_DIR}/scripts/sign-file sha512 ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.pem ${STAGING_KERNEL_BUILDDIR}/certs/signing_key.x509 ${i}
        done
    fi
}

addtask do_module_signing after do_package before do_package_write_ipk

# The inherit of module.bbclass will automatically name module packages with
# kernel-module-" prefix as required by the oe-core build environment. Also it
# replaces '_' with '-' in the module name.

AUDIO_DLKM_MODULES = " \
    q6 spf-core audpkt-ion q6-notifier adsp-loader audio-prm q6-pdr gpr \
    audio-pkt pinctrl-lpi swr swr-ctrl snd-event machine \
    wcd-core mbhc wcd9xxx stub wcd937x wcd937x-slave \
    wsa881x-analog pm2250-spmi rouleur rouleur-slave \
    bolero-cdc va-macro rx-macro tx-macro \
    lpass-cdc lpass-cdc-rx-macro wcd938x \
    wcd939x wcd939x-slave wsa883x wsa884x wcd9378 sdca-registers \
"

RPROVIDES:${PN} += "${@' '.join(['kernel-module-%s-dlkm-%s' % (m, d.getVar('KERNEL_VERSION')) for m in (d.getVar('AUDIO_DLKM_MODULES') or '').split()]).replace('_', '-')}"


