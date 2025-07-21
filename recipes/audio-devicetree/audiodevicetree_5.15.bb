DESCRIPTION      = "QTI Audio devicetree"
LICENSE          = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

inherit linux-kernel-base deploy

PR = "r0"
PACKAGE_ARCH = "${MACHINE_ARCH}"

FILESEXTRAPATHS:prepend := "${WORKSPACE}:"
SRC_URI += "file://vendor/qcom/opensource/audio-devicetree/"
S = "${WORKDIR}/vendor/qcom/opensource/audio-devicetree"
DEPENDS += "virtual/kernel"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

do_compile[depends]   += "virtual/kernel:do_shared_workdir"
do_compile[cleandirs] += "${WORKDIR}/out/${KERNEL_DEFCONFIG}"

do_configure () {
	:
}

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    BUILD_CONFIG=msm-kernel/${KERNEL_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${WORKDIR}/out/${KERNEL_DEFCONFIG} \
    INPLACE_COMPILE=y \
    MODULE_OUT=${S} \
    KBUILD_OPTIONS+="ANDROID_BUILD_TOP=${WORKSPACE}" \
    ./build/build_module.sh
}

do_deploy() {
    install -d ${DEPLOYDIR}/tech_dtbs/
    install -m 0644 ${B}/*.dtbo ${DEPLOYDIR}/tech_dtbs
}

addtask do_deploy after do_install
ALLOW_EMPTY:${PN} = "1"
