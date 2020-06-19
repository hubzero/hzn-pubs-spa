# hubpubs-app

This is a Javascript based web app written in ClojureScript to work with the com\_pubs Hubzero CMS component, and the hubzero-pubs micro-service.

It mounts a [React](https://reactjs.org) component in the Hubzero CMS component.

## Config

At the time of writing, I was the only person really mucking with this ... so double check paths in the config/build file `shadow-cljs.edn`.

## Dependencies

Some dependencies are npm based and can be installed npm style.

    $ npm install

## Building

Here are some examples, check [shadow-cljs](http://shadow-cljs.org/) for more.

    $ shadow-cljs watch app

    $ shadow-cljs compile app

    $ shadow-cljs release app

## REPL

    $ shadow-cljs cljs-repl app

It can be handy to maintain an "up-arrow" history ...

    $ rlwrap shadow-cljs cljs-repl app

## Running

You can do all sorts of trickery for development environment purposes ... but at the end of the day, there needs to be a `main.js` in `app/componentes/com_pubs/site/assets/js/`.

*You will need to change this for your environment in the `shadow-cljs.edn` file.*


