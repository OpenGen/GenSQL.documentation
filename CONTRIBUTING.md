# Contributing

## Prerequisites

Install the following tools:

* [Babashka](https://github.com/babashka/babashka#installation)
* [Clojure](https://clojure.org/guides/install_clojure)
* Java
* [pnpm](https://pnpm.io/installation)

## Build

1. Install the prerequisites.
1. Clone or fork the repository.
1. Build the site with `bb build:site`.

You can then view the files locally in your browser:

```bash
bb run:files
```

You can also run the web server without containerization:

```bash
bb run:host
```

## Deploy

Build and run the Docker image to verify that it works as expected:

```bash
bb build:docker
bb run:docker
```

Deploy to [fly.io](https://fly.io):

```bash
bb deploy:fly
```
