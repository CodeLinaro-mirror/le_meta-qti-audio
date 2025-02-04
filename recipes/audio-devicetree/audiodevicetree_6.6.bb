DESCRIPTION      = "QTI Audio devicetree"
LICENSE          = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

inherit linux-kernel-base deploy

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "file://vendor/qcom/opensource/audio-devicetree/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-devicetree"
EXT_MODULES = "${@os.path.relpath("${S}", "${KERNEL_PLATFORM_PATH}")}"

EXTRA_OEMAKE += "TARGET_SUPPORT=${BASEMACHINE}"

KERNEL_VERSION = "${@get_kernelversion_file("${STAGING_KERNEL_BUILDDIR}")}"

#PARALLEL_MAKE = "-j1"
#RM_WORK_EXCLUDE += "${PN}"

do_configure[noexec] = "1"
do_configure[depends] = "virtual/kernel:do_shared_workdir"
do_compile[lockfiles] = "${TMPDIR}/techpack-dtbs-compile.lock"

do_compile() {
    cd ${KERNEL_PLATFORM_PATH}
    ENABLE_DDK_BUILD=${ENABLE_DDK_BUILD} \
    TARGET_BOARD_PLATFORM=${TARGET_BOARD_PLATFORM} \
    KBUILD_OPTIONS+="TARGET_SUPPORT=${BASEMACHINE}" \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=${EXT_MODULES} \
    KERNEL_KIT=${KERNEL_PREBUILT_PATH} \
    OUT_DIR=${KERNEL_OUT_PATH} \
    MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/audio-devicetree \
    KERNEL_UAPI_HEADERS_DIR=${STAGING_KERNEL_BUILDDIR} \
    TARGET_BOARD_PLATFORM=sa510m \
    ./build/build_module.sh
}

do_deploy() {
    install -d ${DEPLOYDIR}/build-artifacts/techpack-dtbos
    install -m 0644 \
    ${WORKDIR}/vendor/qcom/opensource/audio-devicetree/*.dtbo \
    ${DEPLOYDIR}/build-artifacts/techpack-dtbos/
}

addtask do_deploy after do_install
