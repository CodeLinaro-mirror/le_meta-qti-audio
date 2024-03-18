inherit autotools pkgconfig

DESCRIPTION = "ALSA Framework Library"
LICENSE = "Apache-2.0"
PR = "r5"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS = "glib-2.0 acdbloader libion"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://qcom-opensource/mm-audio/"

S = "${WORKDIR}/qcom-opensource/mm-audio/"

EXTRA_OECONF += "--prefix=/etc \
                 --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                 --with-glib \
                 --with-acdb"

EXTRA_OEMAKE:mdm9607 += "DEFAULT_INCLUDES= CPPFLAGS="-I. -I${PKG_CONFIG_SYSROOT_DIR}/usr/include/acdbloader""

FILES_${PN} += "${prefix}/snd_soc_msm/*"

