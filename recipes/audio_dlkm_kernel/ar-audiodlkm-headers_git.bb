SUMMARY = "Audio Drivers Kernel Headers for AudioReach"
DESCRIPTION = "This is for install Headers"

LICENSE = "GPL-2.0-only"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=801f80980d171dd6425610833a22dbe6"
SRCREV = "${AUTOREV}"

DEPENDS += "virtual/kernel"

PR = "r0"

FILESPATH   =+ "${WORKSPACE}:"
do_configure[noexec] = "1"
do_compile[noexec] = "1"

SRC_URI = "file://vendor/qcom/opensource/audio-kernel/"
S = "${WORKDIR}/vendor/qcom/opensource/audio-kernel"

do_install() {
    install -d -p ${D}${includedir}/audio-kernel/audio/linux
    install -d -p ${D}${includedir}/audio-kernel/audio/sound
    install -d -p ${D}${includedir}/bindings
    install -d -p ${D}${includedir}/soc

    install ${S}/include/uapi/audio/linux/*.h ${D}${includedir}/audio-kernel/audio/linux
    install ${S}/include/uapi/audio/sound/*.h ${D}${includedir}/audio-kernel/audio/sound
    install ${S}/include/bindings/*.h ${D}${includedir}/bindings
    install ${S}/include/soc/*.h ${D}${includedir}/soc
}
