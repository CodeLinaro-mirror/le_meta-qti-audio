inherit autotools pkgconfig

DESCRIPTION = "qahw"
SECTION = "multimedia"
LICENSE = "BSD-3-Clause & BSD-3-Clause-Clear"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause;md5=550794465ba0ec5312d6919e203a55f9 \
                    file://${COREBASE}/meta/files/common-licenses/BSD-3-Clause-Clear;md5=7a434440b651f4a472ca93716d01033a"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI  = "file://hardware/qcom/audio/qahw/"

S = "${WORKDIR}/hardware/qcom/audio/qahw/"
PR = "r0"

DEPENDS = "libhardware liblog libcutils system-media"

EXTRA_OECONF = "--with-glib"
EXTRA_OECONF:append_apq8009 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"
EXTRA_OECONF:append_apq8017 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
