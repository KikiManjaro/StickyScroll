<!-- Plugin description -->
StickyScroll brings VS Code's Sticky Scroll to JetBrains IDEs — the current scope stays pinned at the top of the editor while you scroll.
<!-- Plugin description end -->

# <img align="center" src="https://plugins.jetbrains.com/files/20114/238052/icon/pluginIcon.svg" height="40" /> StickyScroll

[![Version](https://img.shields.io/jetbrains/plugin/v/20114-stickyscroll)](https://plugins.jetbrains.com/plugin/20114-stickyscroll/versions)
[![Rating](https://img.shields.io/jetbrains/plugin/r/rating/20114-stickyscroll)](https://plugins.jetbrains.com/plugin/20114-stickyscroll/reviews)
[![Downloads](https://img.shields.io/jetbrains/plugin/d/20114-stickyscroll)](https://plugins.jetbrains.com/plugin/20114-stickyscroll)
[![Build](https://github.com/KikiManjaro/StickyScroll/actions/workflows/build.yml/badge.svg)](https://github.com/KikiManjaro/StickyScroll/actions/workflows/build.yml)

VS Code Sticky Scroll for JetBrains IDEs — see at a glance which class / method / scope you are scrolling through.

https://user-images.githubusercontent.com/59285425/209567993-2ba8239f-1d19-4037-9107-a4f54abb54eb.mp4

> **Note:** JetBrains now ships [native Sticky Lines](https://youtrack.jetbrains.com/issue/IJPL-449/Sticky-Lines) (2023.3+). This plugin was built as a proof-of-concept before that landed and is no longer actively maintained, but remains available for older IDE versions and for anyone who prefers its behavior.

## Features

- Pins the enclosing scopes (class, method, function, etc.) at the top of the editor
- Click a pinned header to jump to its declaration
- Configurable maximum number of pinned lines (Settings → Tools → StickyScroll)

## Supported languages

| Language | Status |
|---|---|
| Java | ✅ Supported |
| Kotlin | ✅ Supported |
| Python | ✅ Supported |
| JSON | ✅ Supported |
| XML / HTML and other XML-based | ✅ Supported |
| Go, Rust, C/C++, C#, JavaScript, TypeScript, CSS, Dart, PHP, YAML, F# | 🔲 Requested — contributions welcome (see open issues) |

> Jupyter notebooks (`.ipynb`) are explicitly excluded to avoid crashes ([#3](https://github.com/KikiManjaro/StickyScroll/issues/3)).

## Installation

**From the Marketplace (recommended):** `Settings / Preferences → Plugins → Marketplace → Search "StickyScroll" → Install`

**From disk:** download the latest release ZIP from [Releases](https://github.com/KikiManjaro/StickyScroll/releases) or the [JetBrains Marketplace](https://plugins.jetbrains.com/plugin/20114-stickyscroll), then `Settings → Plugins → ⚙️ → Install Plugin from Disk`.

## Configuration

`Settings → Tools → StickyScroll → Maximum lines` — how many scopes to pin (0–10, default 10).

## Known issues & roadmap

- [#5](https://github.com/KikiManjaro/StickyScroll/issues/5) Javadoc can dominate the pinned area
- [#14](https://github.com/KikiManjaro/StickyScroll/issues/14) Git blame gutter interaction
- [#21](https://github.com/KikiManjaro/StickyScroll/issues/21) Pinned fragment vs. editor header z-order
- [#23](https://github.com/KikiManjaro/StickyScroll/issues/23) Annotations shown before class header

See the [open issues](https://github.com/KikiManjaro/StickyScroll/issues) for the full list and language requests.

## Development

```bash
./gradlew runIde          # run a sandboxed IDE with the plugin
./gradlew test            # unit tests
./gradlew buildPlugin     # build distribution ZIP
./gradlew runPluginVerifier
```

Requires JDK 17 and Gradle 8.5 (see `gradle.properties`).

## Contributing

Contributions are welcome! Please read [CONTRIBUTING.md](CONTRIBUTING.md) and [CODE_OF_CONDUCT.md](CODE_OF_CONDUCT.md) first.

## License

No explicit license file is present — please open an issue if you need one clarified.

---

[![Buy Me a Coffee](https://img.buymeacoffee.com/api/?url=aHR0cHM6Ly9pbWcuYnV5bWVhY29mZmVlLmNvbS9hcGkvP3VybD1hSFIwY0hNNkx5OWpaRzR1WW5WNWJXVmhZMjltWm1WbExtTnZiUzkxY0d4dllXUnpMM0J5YjJacGJHVmZjR2xqZEhWeVpYTXZNakF5TVM4d015ODBZekkwT0RnNE1XWmxOVE5pWmprM1lUa3pOV1kxWm1NNFlqRXpPV1EyTWk1d2JtYz0mc2l6ZT0zMDAmbmFtZT1raWtpbWFuamFybw==&creator=kikimanjaro&is_creating=creating%20mobile%20apps%20and%20plugins&design_code=1&design_color=%23ff813f&slug=kikimanjaro)](https://www.buymeacoffee.com/kikimanjaro)
