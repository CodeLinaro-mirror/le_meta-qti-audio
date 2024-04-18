include $(TOPDIR)/rules.mk
include $(INCLUDE_DIR)/kernel.mk

PKG_NAME:=audio-devicetree
PKG_VERSION:=1.0
PKG_RELEASE:=1
PKG_LICENSE:=BSD-3-Clause
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/audio-devicetree

include $(INCLUDE_DIR)/package.mk

define KernelPackage/audio-devicetree
  SECTION:=Kernel Modules
  CATEGORY:=External Kernel Modules
  TITLE:=AUDIO-DEVICETREE
endef

define KernelPackage/audio-devicetree/Description
	AUDIO-DEVICETREE
endef

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
	$(CP) $(STAGING_DIR)/usr/include/audio-kernel/* $(PKG_BUILD_DIR)/
endef

MAKE_OPTS:= \
    M="$(PKG_BUILD_DIR)" \
    TARGET_SUPPORT="$(KERNEL_PLATFORM_TARGET)"\

define Build/Compile
	$(MAKE) -C "$(LINUX_DIR)" \
	$(MAKE_OPTS) \
	modules
endef

define Build/InstallDev
	@true
endef

define KernelPackage/audio-devicetree/install
	mkdir -p $(LINUX_DIR)/../techpack-dtbos
	$(CP) $(PKG_BUILD_DIR)/*.dtbo $(LINUX_DIR)/../techpack-dtbos/
endef

$(eval $(call KernelPackage,audio-devicetree))
