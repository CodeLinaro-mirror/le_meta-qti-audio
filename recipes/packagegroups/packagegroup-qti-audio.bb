#SUMMARY = "QTI Audio Package Group"
SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause & (GPL-2.0 | GPL-2.0-only) "

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

RDEPENDS:packagegroup-qti-audio += ' \
    tinyalsa \
    tinycompress \
    ${@bb.utils.contains("BASEMACHINE", "sa525m", "", "audiohal", d)} \
    ${@bb.utils.contains("BASEMACHINE", "sa525m", "", "qahw", d)} \
    ${@bb.utils.contains("BASEMACHINE", "sa525m", "audiodlkm", "", d)} \
    init-audio \
'
