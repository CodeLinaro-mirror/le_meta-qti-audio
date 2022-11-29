#SUMMARY = "QTI Audio Package Group"
SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause & LGPL-2.1 "

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

RDEPENDS_packagegroup-qti-audio += ' \
    tinyalsa \
    tinycompress \
    ${@bb.utils.contains("BASEMACHINE", "sa525m", "", "audiohal", d)} \
    ${@bb.utils.contains("BASEMACHINE", "sa525m", "", "qahw", d)} \
    ${@bb.utils.contains("BASEMACHINE", "sa525m", "audiodlkm", "", d)} \
    init-audio \
'
