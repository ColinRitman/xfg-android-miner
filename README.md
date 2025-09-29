# XFG ANDROID MINER

The first optimized android miner for mining XFG on Fuego L1 privacy blockchain network. 

Built by advocates for freedom through sound money & free open-source software.


# Usage

This will only work on devices with ARM64 architecture.

Open the app, enter your XFG address (starting with 'fire'), select pool, start mining!

# Notes

The xmrig binary is copied to the app's internal directory along with its dependent libraries.
(Because it can only be executed there)

The binary is started using the ProcessBuilder class, and the output is captured
into the app's scrolling pane once each secons.

Currently only arm64 binaries are included, and the app will refuse to work on
other architectures like x86 or 32 bit devices.

## Acknowledgement

Uplexa developers
