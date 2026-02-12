tidy:
	cd native/warpnet-client && go mod tidy && cd -

build:
	/bin/bash /home/vadim/go/src/github.com/Warp-net/warpnet-android/scripts/build-native.sh
