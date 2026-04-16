inherit autotools-brokensep pkgconfig

DESCRIPTION = "encoders"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=550794465ba0ec5312d6919e203a55f9"

PR = "r0"

FILESPATH   =+ "${WORKSPACE}/:"
SRC_URI     =  "file://vendor/qcom/opensource/audio-hal/primary-hal/mm-audio"

S = "${WORKDIR}/vendor/qcom/opensource/audio-hal/primary-hal/mm-audio"
EXTRA_OECONF:append += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF:append += "--with-glib"


DEPENDS = "glib-2.0 system-core media"

DEPENDS:append = "${@bb.utils.contains('DISTRO_FEATURES', 'audio-dlkm', ' audiodlkm', '', d)}"

RDEPENDS:${PN} = "media"

SOLIBS = ".so"
FILES_SOLIBSDEV = ""
