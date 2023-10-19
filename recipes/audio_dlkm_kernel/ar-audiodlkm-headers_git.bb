SUMMARY = "Audio Drivers Kernel Headers for AudioReach"
DESCRIPTION = "This is for install Headers"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"
SRCREV = "${AUTOREV}"

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

SRC_URI = "file://vendor/qcom/opensource/audio-kernel/"
S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

do_install() {
    install -d -p ${D}${includedir}/audio-kernel/audio/linux
    install -d -p ${D}${includedir}/audio-kernel/audio/linux/mfd/wcd9xxx
    install -d -p ${D}${includedir}/audio-kernel/audio/sound

    process_headers "${S}/include/uapi/audio/linux" "${D}${includedir}/audio-kernel/audio/linux"
    process_headers "${S}/include/uapi/audio/linux/mfd/wcd9xxx" "${D}${includedir}/audio-kernel/audio/linux/mfd/wcd9xxx"
    process_headers "${S}/include/uapi/audio/sound" "${D}${includedir}/audio-kernel/audio/sound"
}

process_headers() {
    cd ${KERNEL_PLATFORM_PATH}/../out/${KERNEL_DEFCONFIG}/msm-kernel/
    for name in $(ls $1/*.h); do
        ${STAGING_KERNEL_DIR}/scripts/headers_install.sh $1/$(basename $name) $2/$(basename $name)
    done
}
