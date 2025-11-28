# Marvus

## About
This is simple application for managing finances. This application use my private proprietary library, that I don't just share, so this code is
just for showcase.

This project is continuing in my older project and it was started on: Apr 29, 2022.

## Design philosophy

The application still uses a flat-file system. Without a robust database, it would lose accuracy because all money calculations are handled by BigDecimal, which preserves decimal precision that would otherwise be lost with the simple double type.

Even though I originally planned to migrate the application to a real database, I decided to abandon that idea and keep the current flat-file system, which effectively "mimics" database behavior.

## Used libraries

*The libraries below are listed in the order they were added to the project.*

| Name | Used for | Included in this repository |
| :------: | :----------: | :---: |
| SufuLib | Swing and other utils | ❌ |
| [MarvusLib](https://github.com/Riyufuchi/MarvusLib) | Reusable stuff from this project | ❌ |


## Donate

[![ko-fi](https://ko-fi.com/img/githubbutton_sm.svg)](https://ko-fi.com/P5P11WTFL)
