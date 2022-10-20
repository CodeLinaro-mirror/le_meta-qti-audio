include $(TOPDIR)/rules.mk
include $(INCLUDE_DIR)/kernel.mk

PKG_NAME:=audio-kernel
PKG_VERSION:=1.0
PKG_RELEASE:=1
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/audio-kernel

include $(INCLUDE_DIR)/package.mk

define KernelPackage/audio-kernel
	SECTION:=Kernel Modules
	CATEGORY:=External Kernel Modules
	TITLE:=AUDIO-KERNEL
endef

define Package/audio-kernel/Description
	AUDIO-KERNEL
endef

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

MAKE_OPTS:= \
    M="$(PKG_BUILD_DIR)" \
    TARGET_SUPPORT="$(KERNEL_PLATFORM_TARGET)"

define Build/Compile
	$(MAKE) -C "$(LINUX_DIR)" \
	$(MAKE_OPTS) \
	modules
endef

define Build/InstallDev
	$(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include/audio-kernel/
	$(CP) $(PKG_BUILD_DIR)/include/* $(1)/usr/include/audio-kernel/
endef

define KernelPackage/audio-kernel/install
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/wcd934x/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/dsp/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/soc/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/ipc/
	$(CP) $(PKG_BUILD_DIR)/asoc/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/
	$(CP) $(PKG_BUILD_DIR)/asoc/codecs/wcd934x/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/wcd934x/
	$(CP) $(PKG_BUILD_DIR)/asoc/codecs/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/
	$(CP) $(PKG_BUILD_DIR)/dsp/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/dsp/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/soc/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/soc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/ipc/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/ipc/
endef

$(eval $(call KernelPackage,audio-kernel))
