include $(TOPDIR)/rules.mk
include $(INCLUDE_DIR)/kernel.mk

PKG_NAME:=audio-kernel-ko
PKG_VERSION:=1.0
PKG_RELEASE:=1
PKG_LICENSE:=GPL-2.0
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/kernel/msm-5.4/techpack/audio

include $(INCLUDE_DIR)/package.mk

define Package/audio-kernel-ko
	SECTION:=AUDIO
	CATEGORY:=AUDIO
	MAINTAINER:=QTI MM Audio
	TITLE:=AUDIO-KERNEL-KO
endef

define Package/audio-kernel-ko/Description
	Copy AUDIO-KERNEL Ko's On Kernel 5.4.
endef

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

define Build/Configure
	@true
endef

define Build/Compile
	@true
endef

define Build/InstallDev
	@true
endef

define Package/audio-kernel-ko/install
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/wcd934x/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/dsp/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/soc/
	$(INSTALL_DIR) $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/ipc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/asoc/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/asoc/codecs/wcd934x/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/wcd934x/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/asoc/codecs/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/asoc/codecs/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/dsp/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/dsp/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/soc/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/soc/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/ipc/*.ko $(1)/lib/modules/$(LINUX_VERSION)-debug/kernel/techpack/audio/ipc/
endef

$(eval $(call BuildPackage,audio-kernel-ko))
