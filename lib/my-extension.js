"use strict";

const child_process = require("child_process");
const fs = require("fs");
const path = require("path");

const repoURL = "https://github.com/OpenIQL/inferenceql.query.git";

const jsFile = function(worktree) {
  return path.join(worktree, "out", "main.js");
}

const isBuilt = function(worktree) {
  return fs.existsSync(jsFile(worktree));
}

const build = function (worktree) {
  console.log(`Building inferenceql.query: ${worktree}…`);
  const stdout = child_process.execSync("clojure -M:js-build", {
    stdio: "inherit",
    cwd: worktree
  });
};

module.exports.register = (context) => {
  context.once("contentAggregated", async ({ siteCatalog, contentAggregate }) => {
    const files = contentAggregate
      .flatMap(aggregate => aggregate.files)
      .filter(file => file.src.origin.url === repoURL);
    const worktrees = [... new Set(files.map(file => file.src.origin.worktree))];
    for (const file of files) {
      const worktree = file.src.origin.worktree;
      if (isBuilt(worktree)) {
        console.log(`Worktree already built: ${worktree}`)
      } else {
        build(worktree);
      }
      const buffer = fs.readFile(jsFile(worktree), (err, data) => {
        if (err) throw err;
        const buffer = Buffer.from(data);
        siteCatalog.addFile({ contents: buffer, out: { path: "inferenceql.query.js" } })
      });
    }
  });
};
