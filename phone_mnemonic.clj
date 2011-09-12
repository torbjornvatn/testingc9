(ns phone-code
  (:require [clojure.set :as set]))

(def words
  (string/split-lines (slurp "/usr/share/dict/words")))

(def mnemonics { \2 "ABC"
                \3 "DEF"
                \4 "GHI"
                \5 "JKL"
                \6 "MNO"
                \7 "PQRS"
                \8 "TUV"
                \9 "WXYZ"})

(def char-code 
  (into {} (for [[digit string] mnemonics
                    letter string]
                [letter digit])))

(defn word-code [word]
  (map char-code (.toUpperCase word)))

(def words-for-num
  (group-by word (filter #(<= (count %) 7) words)))

(def encode 
    "Given phone number digits as a string, return a set of possible
    word phrases which can be made from it."
    [number]
    (reduce clojure.set/union
        (for [split-point (range (count number))]
            (let [[first-words second-words] (map words-for-num (split-at split-point number))]
                (if (seq first-words)
                    (set (for [first-word first-words
                                second-word second-words]
                            (str first-word " " second-word)))
                        (set second-words))))))
    
(comment
  (encode "98219397"))    