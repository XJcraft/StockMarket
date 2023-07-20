# StockMarket
this is a plugin to simulate stock market, the default currency is emerald.
Developping
```
StockMarket
├─ .git
│  ├─ config
│  ├─ description
│  ├─ FETCH_HEAD
│  ├─ HEAD
│  ├─ hooks
│  │  ├─ applypatch-msg.sample
│  │  ├─ commit-msg.sample
│  │  ├─ fsmonitor-watchman.sample
│  │  ├─ post-update.sample
│  │  ├─ pre-applypatch.sample
│  │  ├─ pre-commit.sample
│  │  ├─ pre-merge-commit.sample
│  │  ├─ pre-push.sample
│  │  ├─ pre-rebase.sample
│  │  ├─ pre-receive.sample
│  │  ├─ prepare-commit-msg.sample
│  │  ├─ push-to-checkout.sample
│  │  └─ update.sample
│  ├─ index
│  ├─ info
│  │  └─ exclude
│  ├─ logs
│  │  ├─ HEAD
│  │  └─ refs
│  │     ├─ heads
│  │     │  ├─ master
│  │     │  └─ StockMarket
│  │     └─ remotes
│  │        └─ origin
│  │           └─ HEAD
│  ├─ objects
│  │  ├─ info
│  │  └─ pack
│  │     ├─ pack-5c4ef11f1377cd743163597563fa73b253813c82.idx
│  │     └─ pack-5c4ef11f1377cd743163597563fa73b253813c82.pack
│  ├─ packed-refs
│  └─ refs
│     ├─ heads
│     │  ├─ master
│     │  └─ StockMarket
│     ├─ remotes
│     │  └─ origin
│     │     └─ HEAD
│     └─ tags
├─ .gitignore
├─ .vscode
│  └─ settings.json
├─ pom.xml
├─ README.md
└─ src
   ├─ main
   │  ├─ java
   │  │  └─ org
   │  │     └─ xjcraft
   │  │        ├─ trade
   │  │        │  ├─ bean
   │  │        │  │  └─ StockCustomItem.java
   │  │        │  ├─ config
   │  │        │  │  ├─ Config.java
   │  │        │  │  ├─ IconConfig.java
   │  │        │  │  ├─ LoreConfig.java
   │  │        │  │  ├─ MessageConfig.java
   │  │        │  │  └─ SpecialItemConfig.java
   │  │        │  ├─ Dao.java
   │  │        │  ├─ entity
   │  │        │  │  ├─ StockHistory.java
   │  │        │  │  ├─ StockStorage.java
   │  │        │  │  └─ StockTrade.java
   │  │        │  ├─ gui
   │  │        │  │  ├─ Bag.java
   │  │        │  │  ├─ Callback.java
   │  │        │  │  ├─ Counter.java
   │  │        │  │  ├─ Menu.java
   │  │        │  │  ├─ Shop.java
   │  │        │  │  └─ StockMarketGui.java
   │  │        │  ├─ StockMarket.java
   │  │        │  ├─ StockMarketCommands.java
   │  │        │  ├─ StockMarketListener.java
   │  │        │  ├─ StockMarketManager.java
   │  │        │  └─ utils
   │  │        │     ├─ InfoUtil.java
   │  │        │     ├─ ItemUtil.java
   │  │        │     ├─ SerializeUtil.java
   │  │        │     └─ StringUtil.java
   │  │        └─ utils
   │  │           └─ JSON.java
   │  └─ resources
   │     ├─ plugin.yml
   │     └─ zh_cn.json
   └─ test
      └─ java
         └─ DecodeTest.java

```