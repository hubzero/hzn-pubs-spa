(ns pubs.subs
  (:require
   [re-frame.core :as rf]))

(rf/reg-sub ::data #(identity %))

