# XFG ANDROID MINER

The first optimized android miner for Fuego (XFG) - privacy-focused cryptocurrency built by advocates for freedom through sound money.

<img src="./screenshots/1.png" height="400" />
<img src="./screenshots/2.png" height="400" />
<img src="./screenshots/3.png" height="400" />

# Usage

This will currently only work on devices with ARM64 architecture.

Run the app, enter your XFG address (starting with 'fire'), select a pool, start mining!

# Notes

The xmrig binary is copied to the app's internal directory along with its dependent libraries.
(Because it can only be executed there)

The binary is started using the ProcessBuilder class, and the output is captured
into the app's scrolling pane once each secons.

Currently only arm64 binaries are included, and the app will refuse to work on
other architectures like x86 or 32 bit devices.
