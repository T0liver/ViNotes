import json
import os
import hashlib
from datetime import date
from pathlib import Path

SOURCE_DIR = Path("source/wines")
OUTPUT_DIR = Path("catalog")
TODAY = date.today().isoformat()  # "2025-05-16"


def load_all_wines() -> list[dict]:
    wines = []
    for json_file in sorted(SOURCE_DIR.rglob("*.json")):
        with open(json_file, encoding="utf-8") as f:
            wines.extend(json.load(f))
    return wines


def sha256_of(data: str) -> str:
    return hashlib.sha256(data.encode()).hexdigest()


def load_existing_full() -> list[dict]:
    full_path = OUTPUT_DIR / "full.json"
    if not full_path.exists():
        return []
    with open(full_path, encoding="utf-8") as f:
        return json.load(f).get("wines", [])


def build_delta(old_wines: list[dict], new_wines: list[dict]) -> dict:
    old_by_id = {w["id"]: w for w in old_wines}
    new_by_id = {w["id"]: w for w in new_wines}

    added = [w for wid, w in new_by_id.items() if wid not in old_by_id]
    updated = [w for wid, w in new_by_id.items()
               if wid in old_by_id and w != old_by_id[wid]]
    removed_ids = [wid for wid in old_by_id if wid not in new_by_id]

    return {
        "type": "delta",
        "date": TODAY,
        "added": added,
        "updated": updated,
        "removedIds": removed_ids
    }


def load_existing_manifest() -> dict:
    manifest_path = OUTPUT_DIR / "manifest.json"
    if not manifest_path.exists():
        return {"schemaVersion": 1, "deltas": []}
    with open(manifest_path, encoding="utf-8") as f:
        return json.load(f)


def write_json(path: Path, data: dict):
    path.parent.mkdir(parents=True, exist_ok=True)
    content = json.dumps(data, ensure_ascii=False, indent=2)
    with open(path, "w", encoding="utf-8") as f:
        f.write(content)
    return content


def main():
    new_wines = load_all_wines()
    old_wines = load_existing_full()

    # full.json
    full_data = {
        "type": "full",
        "exportedAt": TODAY,
        "wines": new_wines
    }
    full_content = write_json(OUTPUT_DIR / "full.json", full_data)
    full_hash = sha256_of(full_content)
    print(f"full.json: {len(new_wines)} wines, sha256: {full_hash[:12]}...")

    # delta - only if there are changes
    manifest = load_existing_manifest()
    delta = build_delta(old_wines, new_wines)
    has_changes = delta["added"] or delta["updated"] or delta["removedIds"]

    if has_changes:
        delta_filename = f"delta-{TODAY}.json"
        delta_content = write_json(OUTPUT_DIR / delta_filename, delta)
        delta_hash = sha256_of(delta_content)

        added_count = len(delta["added"])
        print(f"{delta_filename}: +{added_count} new, "
              f"{len(delta['updated'])} changed, "
              f"{len(delta['removedIds'])} deleted")

        # add delta to manifest if not already present
        existing_dates = {d["date"] for d in manifest.get("deltas", [])}
        if TODAY not in existing_dates:
            manifest.setdefault("deltas", []).insert(0, {
                "date": TODAY,
                "url": delta_filename,
                "addedCount": added_count,
                "sha256": delta_hash
            })
    else:
        print("No change, delta skipped.")

    # update manifest.json
    manifest["schemaVersion"] = 1
    manifest["lastUpdated"] = TODAY
    manifest["full"] = {
        "url": "full.json",
        "wineCount": len(new_wines),
        "sha256": full_hash
    }
    write_json(OUTPUT_DIR / "manifest.json", manifest)
    print("manifest.json updated.")


if __name__ == "__main__":
    main()