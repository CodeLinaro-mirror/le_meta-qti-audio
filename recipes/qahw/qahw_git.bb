inherit autotools pkgconfig

DESCRIPTION = "qahw"
SECTION = "multimedia"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

FILESPATH =+ "${WORKSPACE}/:"
SRC_URI  = "file://vendor/qcom/opensource/audio-hal/primary-hal/qahw/"

S = "${WORKDIR}/vendor/qcom/opensource/audio-hal/primary-hal/qahw/"
PR = "r0"

DEPENDS = "libhardware liblog libcutils media-headers audio-utils glib-2.0"

EXTRA_OECONF = "--with-glib"
EXTRA_OECONF:append:apq8009 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"
EXTRA_OECONF:append:apq8017 = " BOARD_SUPPORTS_SVA_AUDIO_CONCURRENCY=true"
EXTRA_OEMAKE += 'DEFAULT_INCLUDES= CFLAGS="${CFLAGS} -O2" \
CPPFLAGS="-I${STAGING_INCDIR}/system -I${STAGING_INCDIR}/hardware \
-I${STAGING_INCDIR}/audio_effects -I${STAGING_INCDIR}"'

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
