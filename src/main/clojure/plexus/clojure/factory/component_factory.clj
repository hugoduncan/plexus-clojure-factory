(ns plexus.clojure.factory.component-factory
  "ComponentFactory for Plexus components implemented in clojure.")

(defn instantiate
  "Instantiate the given class name"
  [name]

  (println "*use-context-classloader*" *use-context-classloader*)

  (println (seq (.getURLs (.getContextClassLoader (Thread/currentThread)))))
  (println (seq (.getURLs (clojure.lang.RT/baseLoader))))

  (let [name (if (.startsWith name "class ")
               (.substring name 6)
               name)
        i (.lastIndexOf name ".")
        the-ns (.substring name 0 i)
        the-class (.substring name (inc i))]
    (println "the-ns" the-ns)
    (require (symbol the-ns))
    (when-let [factory (find-var (symbol the-ns (str "make-" the-class)))]
      (factory))))
