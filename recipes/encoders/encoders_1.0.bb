inherit autotools qcommon

DESCRIPTION = "encoders"
LICENSE = "BSD"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/\
${LICENSE};md5=3775480a712fc46a69647678acb234cb"

PR = "r0"

SRC_DIR = "${WORKSPACE}/hardware/qcom/audio/mm-audio/"

HALBINSUFFIX = "${@base_contains('TUNE_ARCH', 'aarch64', '_64bit', '', d)}"
EXTRA_OEMAKE = "DEFAULT_INCLUDES=-I${STAGING_KERNEL_BUILDDIR}/usr/techpack/audio/include"
S = "${WORKDIR}/hardware/qcom/audio/mm-audio/"
EXTRA_OECONF_append += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF_append += "--with-glib"
EXTRA_OECONF_append += "--program-suffix=${HALBINSUFFIX}"

DEPENDS = "media"
RDEPENDS_${PN} = "media"

FILES_${PN}-dbg  = "${libdir}/.debug/*"
FILES_${PN}      = "${libdir}/*.so ${libdir}/*.so.* ${sysconfdir}/* ${bindir}/* ${libdir}/pkgconfig/*"
FILES_${PN}-dev  = "${libdir}/*.la ${includedir}"
INSANE_SKIP_${PN} = "dev-so"
