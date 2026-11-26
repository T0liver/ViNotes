# Changelog

All notable changes to ViNotes are documented in this file.

## [1.1.0] — 2026-11-26

Bug-fix and polish release merging PRs #24–#31.

### Fixed

- **#10** — Prevent accidental dismissal of add/edit wine and filter bottom sheets when the form has unsaved changes (#27).
- **#17** — Catalogue re-import no longer deletes tastings or overwrites user-edited wine fields (#24).
- **#18** — Wine colour filter chips wrap on narrow screens instead of being clipped (#28).
- **#19** — Accept `ROSÉ` and accent-insensitive catalogue colour values on file and web import (#25).
- **#20** — Dashboard loading spinner clears after “Delete all data” (#26).
- **#21** — Declare location hardware optional via `uses-feature` for correct Play Store filtering (#29).
- **#22** — Hungarian Stats strings, localized period chips and heatmap weekdays, and assorted UX fixes (#30).
- **#23** — Rating arc slider no longer jumps to 100 when dragging below the left end (#31).