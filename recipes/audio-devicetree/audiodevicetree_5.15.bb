DESCRIPTION      = "QTI Audio devicetree"
LICENSE          = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9"

inherit linux-kernel-base deploy

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
SRC_URI = "\
    file://vendor/qcom/opensource/audio-devicetree/ \
"

S = "${WORKDIR}/vendor/qcom/opensource/audio-devicetree"

RM_WORK_EXCLUDE += "${PN}"

do_configure[noexec] = "1"
do_configure[depends] = "virtual/kernel:do_shared_workdir"
do_compile[lockfiles] = "${TMPDIR}/techpack-dtbs-compile.lock"

do_compile() {
    cd ${WORKSPACE}/kernel-${PREFERRED_VERSION_linux-msm}/kernel_platform  && \
    BUILD_CONFIG=${KERNEL_BUILD_CONFIG} \
    EXT_MODULES=../../vendor/qcom/opensource/audio-devicetree \
    ROOTDIR=${WORKDIR}/ \
    TARGET_SUPPORT=${BASEMACHINE} \
    MODULE_OUT=${WORKDIR}/vendor/qcom/opensource/audio-devicetree \
    KBUILD_OPTIONS+="ANDROID_BUILD_TOP=${WORKSPACE}" \
    OUT_DIR=${KERNEL_OUT_PATH}/ \
    ./build/build_module.sh
}

do_deploy() {
    install -d ${DEPLOYDIR}/tech_dtbs/
    install -m 0644 \
    ${WORKDIR}/vendor/qcom/opensource/audio-devicetree/*.dtbo \
    ${DEPLOYDIR}/tech_dtbs/
}

addtask do_deploy after do_install
ALLOW_EMPTY:${PN} = "1"
