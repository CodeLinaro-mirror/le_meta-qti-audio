inherit autotools-brokensep pkgconfig

DESCRIPTION = "encoders"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

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
