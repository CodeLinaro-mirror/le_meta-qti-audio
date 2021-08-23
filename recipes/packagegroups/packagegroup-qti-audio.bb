SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause"

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

RDEPENDS_packagegroup-qti-audio += ' \
    audiodlkm \
    init-audio \
    tinyalsa \
    tinycompress \
'
