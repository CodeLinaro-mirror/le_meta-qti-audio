inherit autotools pkgconfig

DESCRIPTION = "qahw"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI  = "file://hardware/qcom/audio/qahw/"

S = "${WORKDIR}/hardware/qcom/audio/qahw/"
PR = "r0"

DEPENDS += "libhardware liblog libcutils media-headers audio-route audio-utils glib-2.0"

EXTRA_OECONF = "--with-glib"
EXTRA_OECONF:append:apq8009 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"
EXTRA_OECONF:append:apq8017 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
