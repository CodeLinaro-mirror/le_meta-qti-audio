#SUMMARY = "QTI Audio Package Group"
SUMMARY = "QTI Audio Package Group"

LICENSE = "BSD-3-Clause & (LGPL-2.1 | LGPL-2.1-only) "

PACKAGE_ARCH = "${MACHINE_ARCH}"

inherit packagegroup

PROVIDES = "${PACKAGES}"

PACKAGES = ' \
    packagegroup-qti-audio \
'

RDEPENDS:packagegroup-qti-audio += ' \
    tinyalsa \
    tinycompress \
    init-audio \
    ${@bb.utils.contains_any("BASEMACHINE", "sa525m sa510m", "", "audiohal", d)} \
    ${@bb.utils.contains_any("BASEMACHINE", "sa525m sa510m", "", "qahw", d)} \
    ${@bb.utils.contains_any("BASEMACHINE", "sa525m sa510m", "audiodlkm", "", d)} \
'
