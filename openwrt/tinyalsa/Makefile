include $(TOPDIR)/rules.mk

PKG_NAME:=tinyalsa
PKG_RELEASE:=1
PKG_LICENSE:=BSD-3-Clause
PKG_FIXUP:=autoreconf

LOCAL_SRC ?= $(TOPDIR)/src/vendor/qcom/opensource/tinyalsa

include $(INCLUDE_DIR)/package.mk

define Package/tinyalsa
  SECTION:=AUDIO
  CATEGORY:=AUDIO
  MAINTAINER:=QTI MM Audio
  TITLE:=TINYALSA
  DEPENDS:= +libcutils +glib2 +sndparser +libutils
endef

define Package/tinyalsa/description
	Tinyalsa Library
endef

CONFIGURE_ARGS += \
                  --with-openwrt \
                  --with-glib

TARGET_CFLAGS += \
                   -DFEATURE_IPQ_OPENWRT

define Build/Prepare
	mkdir -p $(PKG_BUILD_DIR)/
	$(CP) $(LOCAL_SRC)/* $(PKG_BUILD_DIR)/
endef

define Build/InstallDev
	$(INSTALL_DIR) $(STAGING_DIR)
	mkdir -p $(1)/usr/include
	mkdir -p $(1)/usr/include/tinyalsa
	mkdir -p $(1)/usr/lib
	mkdir -p $(1)/usr/bin
	$(CP) $(PKG_BUILD_DIR)/*.h $(1)/usr/include/tinyalsa
	$(CP) $(PKG_BUILD_DIR)/include/tinyalsa/*.h $(1)/usr/include/tinyalsa
	$(CP) $(PKG_BUILD_DIR)/.libs/lib* $(1)/usr/lib/
	$(CP) $(PKG_BUILD_DIR)/.libs/qtitinycap $(1)/usr/bin/
	$(CP) $(PKG_BUILD_DIR)/.libs/qtitinyhostless $(1)/usr/bin/
	$(CP) $(PKG_BUILD_DIR)/.libs/qtitinymix $(1)/usr/bin/
	$(CP) $(PKG_BUILD_DIR)/.libs/qtitinypcminfo $(1)/usr/bin/
	$(CP) $(PKG_BUILD_DIR)/.libs/qtitinyplay $(1)/usr/bin/
endef

define Package/tinyalsa/install
	$(INSTALL_DIR) $(1)/usr/bin/
	$(INSTALL_DIR) $(1)/usr/lib/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/qtitinycap $(1)/usr/bin/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/qtitinyhostless $(1)/usr/bin/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/qtitinymix $(1)/usr/bin/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/qtitinypcminfo $(1)/usr/bin/
	$(INSTALL_BIN) $(PKG_BUILD_DIR)/.libs/qtitinyplay $(1)/usr/bin/
	$(CP) $(PKG_BUILD_DIR)/.libs/lib* $(1)/usr/lib/
endef

$(eval $(call BuildPackage,tinyalsa))
