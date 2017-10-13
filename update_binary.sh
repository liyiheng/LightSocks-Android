#!/usr/bin/env bash
cd lightsocks/cmd/lightsocks-local/
gox -arch='arm' -os='linux'
cd -
mv lightsocks/cmd/lightsocks-local/lightsocks-local_linux_arm app/src/main/res/raw/lightsocks