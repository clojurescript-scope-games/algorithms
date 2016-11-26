## algorithms

### Usage

#### Install Exponent App from Apple Store or Play Store

#### Install [Lein](http://leiningen.org/#install)

#### Install npm modules

``` shell
npm install
```

#### Start the figwheel server
``` shell
    lein figwheel
```

#### Start Exponent server on Android

``` shell
exp start -a --lan
```

#### Or start Exponent server on iOS Simulator

``` shell
exp start -i --lan
```

### Add new assets or external modules
1. `require` module:

``` clj
    (def cljs-logo (js/require "./assets/images/cljs.png"))
    (def FontAwesome (js/require "@exponent/vector-icons/FontAwesome"))
```
2. Reload simulator or device

### Make sure you disable live reload from the Developer Menu, also turn off Hot Module Reload.
Since Figwheel already does those.

### Production build (generates js/externs.js and main.js)

``` shell
lein prod-build
```
