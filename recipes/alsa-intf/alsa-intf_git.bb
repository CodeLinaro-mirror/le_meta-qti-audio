inherit autotools pkgconfig

DESCRIPTION = "ALSA Framework Library"
LICENSE = "Apache-2.0"
PR = "r5"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/files/common-licenses/${LICENSE};md5=89aea4e17d99a7cacdbeed46a0096b10"
DEPENDS = "acdbloader glib-2.0 libion"

FILESPATH =+ "${WORKSPACE}:"
SRC_URI = "file://qcom-opensource/mm-audio/"

S = "${WORKDIR}/qcom-opensource/mm-audio/"

EXTRA_OECONF += "--prefix=/etc \
                 --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include \
                 --with-glib \
                 --with-acdb"

AUDIO_KERNEL_HEADERS="${STAGING_KERNEL_BUILDDIR}/audio-kernel"
CFLAGS += "-I${AUDIO_KERNEL_HEADERS}"

EXTRA_OEMAKE = "DEFAULT_INCLUDES= CPPFLAGS="-I. -I${STAGING_KERNEL_BUILDDIR}/usr/include -I${STAGING_KERNEL_BUILDDIR}/usr/techpack/audio/include""
EXTRA_OECONF += "--prefix=/etc"
EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"
EXTRA_OECONF += "--with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/techpack/audio/include"
EXTRA_OECONF += " --with-audio-kernel-headers=${AUDIO_KERNEL_HEADERS}"
EXTRA_OECONF += ""DEFAULT_INCLUDES= CPPFLAGS= "-I."
EXTRA_OECONF += " --with-ion=${PKG_CONFIG_SYSROOT_DIR}/usr/include/ion_headers/ion"
FILES_${PN} += "${prefix}/snd_soc_msm/*"
FILES_${PN} += "${libdir}/libamrnbtest.so ${prefix}/*"
FILES_${PN} += "${libdir}/libamrwbtest.so ${prefix}/*"
FILES_${PN} += "${libdir}/libamrwbplustest.so ${prefix}/*"

do_install_append_msm8610() {
    mv ${D}/usr/bin/aplay ${D}/usr/bin/qc-aplay
    mv ${D}/usr/bin/amix ${D}/usr/bin/qc-amix
    mv ${D}/usr/bin/arec ${D}/usr/bin/qc-arec
    mv ${D}/usr/bin/alsaucm_test ${D}/usr/bin/qc-alsaucm_test
}

do_install_append_msm8226() {
    mv ${D}/usr/bin/aplay ${D}/usr/bin/qc-aplay
    mv ${D}/usr/bin/amix ${D}/usr/bin/qc-amix
    mv ${D}/usr/bin/arec ${D}/usr/bin/qc-arec
    mv ${D}/usr/bin/alsaucm_test ${D}/usr/bin/qc-alsaucm_test
}
