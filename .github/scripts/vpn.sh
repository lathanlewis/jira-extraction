#!/usr/bin/env sh

waitForKvnet() {
    while ! ip a show kvnet up | grep inet  2>/dev/null; do
        sleep 1
    done
}

writeKerioConfigParam() {
  name=$1
  type=$2
  value=$3
  echo "kerio-control-vpnclient-${VPN_CLIENT_VERSION}-linux-amd64 kerio-kvc/$name $type $value" >> kerio.params
}
writeKerioConfig() {
  writeKerioConfigParam server string central-kerio-vpn.devfactory.com
  writeKerioConfigParam username string $VPN_USERNAME
  writeKerioConfigParam password string $VPN_PASSWORD
  writeKerioConfigParam autodetect_fingerprint boolean true
  writeKerioConfigParam autodetect_accept boolean true
}

sudo apt update && sudo apt install -y wget curl debconf openssl

cd /tmp

writeKerioConfig

wget http://cdn.kerio.com/dwn/control/control-${VPN_CLIENT_VERSION}/kerio-control-vpnclient-${VPN_CLIENT_VERSION}-linux-amd64.deb

sudo debconf-set-selections kerio.params
sudo dpkg -i /tmp/kerio-control-vpnclient-${VPN_CLIENT_VERSION}-linux-amd64.deb
sudo /etc/init.d/kerio-kvc start

waitForKvnet

curl -s --cookie "TOTP_CONTROL=$VPN_AUTH_CODE" http://10.212.255.245:4080//nonauth/totpVerify.cs
