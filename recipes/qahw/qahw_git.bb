inherit autotools pkgconfig

DESCRIPTION = "qahw"
SECTION = "multimedia"
LICENSE = "BSD-Source-Code"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=fe8b41221d7524c70688f7d059ff6d87"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI  = "file://hardware/qcom/audio/qahw/"

S = "${WORKDIR}/hardware/qcom/audio/qahw/"
PR = "r0"

DEPENDS = "libhardware liblog libcutils media-headers audio-route audio-utils glib-2.0"
DEPENDS:append:mdm9607 += "libutils"

EXTRA_OECONF = "--with-glib"
EXTRA_OECONF:append_apq8009 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"
EXTRA_OECONF:append_apq8017 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
