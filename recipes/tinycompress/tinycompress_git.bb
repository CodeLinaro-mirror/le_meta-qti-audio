inherit autotools pkgconfig

DESCRIPTION = "Tinycompress Library"
LICENSE = "BSD & LGPLv2.1"
LIC_FILES_CHKSUM = "file://COPYING;md5=cf9105c1a2d4405cbe04bbe3367373a0"

PR = "r1"

SRCREV = "5de00c7a52325158072edf128b87319328bb930d"
SRC_URI = "git://git.codelinaro.org/clo/le/platform/external/tinycompress.git;protocol=https;branch=caf_migration/alsa-project/master\
           file://0001-compress-add-support-for-plugins.patch"

S = "${WORKDIR}/git"

EXTRA_OEMAKE = "DEFAULT_INCLUDES=-I${WORKDIR}/git/include/"
EXTRA_OECONF_append = " --with-sanitized-headers=${STAGING_KERNEL_BUILDDIR}/usr/include"

DEPENDS = "virtual/kernel glib-2.0"
EXTRA_OECONF += "--with-glib"
