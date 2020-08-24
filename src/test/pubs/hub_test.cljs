(ns pubs.hub-test
  (:require [re-frame.core :as rf :refer [dispatch dispatch-sync]]
            )
  )

(def resps {:me {:id 1001, :username "jbg"} 
            :master-types [{:description "uploaded material", :supporting 1, :params "peer_review=1", :curation nil, :type "File(s)", :curatorgroup nil, :ordering 1, :alias "files", :id 1, :contributable 1} {:description "external content", :supporting 0, :params "", :curation nil, :type "Link", :curatorgroup nil, :ordering 3, :alias "links", :id 2, :contributable 0} {:description "from project notes", :supporting 0, :params "", :curation nil, :type "Wiki", :curatorgroup nil, :ordering 5, :alias "notes", :id 3, :contributable 0} {:description "simulation tool", :supporting 0, :params "", :curation nil, :type "Application", :curatorgroup nil, :ordering 4, :alias "apps", :id 4, :contributable 0} {:description "publication collection", :supporting 0, :params "", :curation nil, :type "Series", :curatorgroup nil, :ordering 6, :alias "series", :id 5, :contributable 0} {:description "image/photo gallery", :supporting 0, :params "", :curation nil, :type "Gallery", :curatorgroup nil, :ordering 7, :alias "gallery", :id 6, :contributable 0} {:description "project database", :supporting 0, :params "", :curation nil, :type "Databases", :curatorgroup nil, :ordering 2, :alias "databases", :id 7, :contributable 0}]
            :pub {:pub-id 209, :ver-id 196, :prj-id 1, :comments "Blha blha blha", :abstract "blah foo bar", :user-id 1001, :state 1, :doi "10.21978/J37M-WH80", :title "Hahaha --- blah blah balh", :master-type 1, :publication-date "04/26/2020", :ack true, :url nil, :license_type nil, :release-notes "hahahahaha"}
            :files {:content {:1264 {:path "broodje/files/c6e1e2e4-c0a0-4e65-b3c2-a66268f40c27.jpg", :name "c6e1e2e4-c0a0-4e65-b3c2-a66268f40c27.jpg", :id 1264, :index 0}}}
            :add-file {:generated_key 1401}
            :rm-file ""
            :ls-files [["broodje/files" ["Foo"] []] ["broodje/files/Foo" ["Bar"] []] ["broodje/files/Foo/Bar" ["Baz"] []] ["broodje/files/Foo/Bar/Baz" [] ["Screenshot_2020-07-02 PURR - PURR.png" "Screen Shot 2020-06-08 at 1.45.09 PM.png"]]]
            :usage {:size "0.0000", :units "GB", :percent "0.00", :max "1.00"}
            :authors {:422 {:email "fb@example.com", :project_owner_id 41, :userid 0, :index 1, :lastname "Blok", :fullname "Femke Blokje", :organization "Homegirl", :firstname "Femke", :id 422, :poc 0, :credit ""}, :431 {:email "ps@example.com", :project_owner_id 42, :userid 0, :index 3, :lastname "Smart", :fullname "Petra Smart", :organization "foo", :firstname "Petra", :id 431, :poc 0, :credit ""}, :452 {:email "jbg@example.com", :project_owner_id 1, :userid 0, :index 0, :lastname "G", :fullname "J B G", :organization "Bob Taco Stand Baz", :firstname "J B", :id 452, :poc 0, :credit ""}, :456 {:email "bos@example.com", :project_owner_id 107, :userid 0, :index 2, :lastname nil, :fullname "Annet Bos", :organization nil, :firstname nil, :id 456, :poc 0, :credit ""}}
            :owners [{:role 1, :projectid 121, :userid 1001, :created_by_user 1001, :lastname "G", :added "2020-04-22T14:16:38Z", :name "J B G", :invited_email "", :username "jbg", :groupname nil, :params nil, :fullname "J B G", :invited_name nil, :invited_code nil, :organization "", :lastvisit nil, :num_visits 0, :firstname "J", :native 1, :groupid 0, :status 1, :id 133, :picture "", :groupdesc nil, :prev_visit nil}]
            :rm-author {:role nil, :repository_contact 0, :project_owner_id 107, :publication_version_id 196, :lastname "Bos", :name "Annet Bos", :created "2020-07-17T10:11:00Z", :organization nil, :modified nil, :ordering 3, :firstname "Annet", :status 1, :id 456, :user_id 0, :modified_by 0, :credit "", :created_by 1001}
            :update-author {:role nil, :repository_contact 0, :project_owner_id 107, :publication_version_id 196, :lastname "Bos", :name "Annet Bos", :created "2020-07-17T10:11:00Z", :organization nil, :modified nil, :ordering 3, :firstname "Annet", :status 1, :id 456, :user_id 0, :modified_by 0, :credit "", :created_by 1001} 
            :add_author {:role nil, :repository_contact 0, :project_owner_id 134, :publication_version_id 196, :lastname nil, :name "Bob Taco", :created "2020-07-17T12:54:07Z", :organization nil, :modified nil, :ordering nil, :firstname nil, :status 1, :id 564, :user_id 0, :modified_by 0, :credit "", :created_by 1001}
            :search-users [{:rel 1, :email "jbg@example.com", :name "J B G", :username "jbg", :surname "G", :middlename "B", :org "Bob Taco Stand", :id 1001, :access 1, :givenname "J"}]
            :prj {:id 1, :alias "prjfoobar", :title "Project Foo Bar"}
            :new-author {:role nil, :email "bt@example.com", :repository_contact 0, :project_owner_id 146, :publication_version_id 214, :lastname "Taco", :name "Bob Taco", :created "2020-08-19T07:12:02Z", :organization "", :modified nil, :ordering nil, :firstname "Bob", :status 1, :id 591, :user_id 0, :modified_by 0, :poc false, :credit "", :created_by 1001}
            :tags [{:description "", :admin 2, :objects 11, :raw_tag "core-foo", :created "2020-01-19T09:48:01Z", :modified "2020-08-19T05:37:07Z", :id 17, :tag "corefoo", :modified_by 1001, :created_by 1000, :substitutes 2}]
            :rm-tag ""
            :add-tag {:generated_key 402}
            :search-tags [{:description "", :admin 2, :objects 11, :raw_tag "core-foo", :created "2020-01-19T09:48:01Z", :modified "2020-08-19T05:37:07Z", :id 17, :tag "corefoo", :modified_by 1001, :created_by 1000, :substitutes 2} {:description "<p>The foo tag.</p>", :admin 0, :objects 111, :raw_tag "foo", :created "2020-01-08T10:44:33Z", :modified "2020-08-17T12:26:24Z", :id 5, :tag "foo", :modified_by 1001, :created_by 1000, :substitutes 0}]
            :licenses [{:restriction nil, :opensource false, :name "cc", :agreement 1, :icon "/components/com_publications/assets/img/logos/cc.gif", :title "CC0 - Creative Commons", :customizable 0, :ordering 2, :active 1, :id 2, :info "CC0 enables scientists, educators, artists and other creators and owners of copyright- or database-protected content to waive those interests in their works and thereby place them as completely as possible in the public domain, so that others may freely build upon, enhance and reuse the works for any purposes without restriction under copyright or database law.", :url "http://creativecommons.org/about/cc0", :main 1, :derivatives 1, :text ""} {:restriction nil, :opensource false, :name "custom", :agreement 0, :icon "/components/com_publications/assets/img/logos/license.gif", :title "Custom", :customizable 1, :ordering 3, :active 1, :id 1, :info "Custom license", :url "http://creativecommons.org/about/cc0", :main 0, :derivatives 0, :text "[ONE LINE DESCRIPTION]\r\nCopyright (C) [YEAR] [OWNER]"}]
            :license {:restriction nil, :opensource false, :name "cc", :agreement 1, :icon "/components/com_publications/assets/img/logos/cc.gif", :title "CC0 - Creative Commons", :customizable 0, :ordering 2, :active 1, :id 2, :info "CC0 enables scientists, educators, artists and other creators and owners of copyright- or database-protected content to waive those interests in their works and thereby place them as completely as possible in the public domain, so that others may freely build upon, enhance and reuse the works for any purposes without restriction under copyright or database law.", :url "http://creativecommons.org/about/cc0", :main 1, :derivatives 1, :text ""}
            :save-pub {:pub-id 227, :ver-id 214}
            :add-citation {:institution "", :date_publish nil, :software_use nil, :address "", :format "apa", :fundedby 0, :publisher "", :key nil, :series "", :number "8", :school "", :short_title "", :custom3 nil, :call_number "", :uid 1292, :formatted "", :res_edu nil, :author_address "", :ref_type "", :params "{\"exclude\":\"\",\"rollover\":1}", :abstract "<p>Using transparent microfluidic cells to study the two-phase properties of a synthetic porous medium, we establish that the interfacial area per volume between nonwetting and wetting fluids lifts the ambiguity associated with the hysteretic relationship between capillary pressure and saturation in porous media. The interface between the nonwetting and wetting phases is composed of two subsets: one with a unique curvature determined by the capillary pressure, and the other with a distribution of curvatures dominated by disjoining pressure. This work provides experimental support for recent theoretical predictions that the capillary-dominated subset plays a role analogous to a state variable. Any comprehensive description of multiphase flow properties must include this interfacial area with the traditional variables of pressure and fluid saturation.</p>", :month "April", :date_accept nil, :type "2", :created "2013-09-24T17:39:09Z", :howpublished "", :organization nil, :isbn "", :scope "hub", :journal "Geophysical Research Letters", :doi "10.1029/2003GL019282", :keywords "", :title "Linking Pressure and Saturation through Interfacial Areas in Porous Media", :pages "", :volume "310", :note "", :author "Cheng, J.-T., Pyrak-Nolte, L. J., Nolte, D. D. and N. J. Giordano", :chapter nil, :year "2004", :date_submit nil, :research_notes "", :language nil, :label nil, :id 1, :notes nil, :eprint "http://www.physics.purdue.edu/rockphys/docs/publications/porous-media/LinkingPressure.pdf", :scope_id "0", :exp_list_exp_data nil, :url "http://onlinelibrary.wiley.com/doi/10.1029/2003GL019282/full", :editor "", :custom2 nil, :affiliated 1, :edition "", :custom1 nil, :location "", :cite "", :published 1, :custom4 nil, :accession_number "", :booktitle "", :exp_data nil}
            :search-citations [{:institution "", :date_publish nil, :software_use nil, :address "", :format nil, :fundedby nil, :publisher "", :key nil, :series "", :number "3", :school "", :short_title "", :custom3 nil, :call_number "", :uid 1292, :formatted nil, :res_edu nil, :author_address "", :ref_type "", :params "exclude=\nrollover=1\n\n", :abstract "Optimizing weighting factors for a linear combination of terms in a scoring function is a crucial step for success in developing a threading algorithm. Usually weighting factors are optimized to yield the highest success rate on a training dataset, and the determined constant values for the weighting factors are used for any target sequence. Here we explore completely different approaches to handle weighting factors for a scoring function of threading. Throughout this study we use a model system of gapless threading using a scoring function with two terms combined by a weighting factor, a main chain angle potential and a residue contact potential. First, we demonstrate that the optimal weighting factor for recognizing the native structure differs from target sequence to target sequence. Then, we present three novel threading methods which circumvent training dataset-based weighting factor optimization. The basic idea of the three methods is to employ different weighting factor values and finally select a template structure for a target sequence by examining characteristics of the distribution of scores computed by using the different weighting factor values. Interestingly, the success rate of our approaches is comparable to the conventional threading method where the weighting factor is optimized based on a training dataset. Moreover, when the size of the training set available for the conventional threading method is small, our approach often performs better. In addition, we predict a target-specific weighting factor optimal for a target sequence by an artificial neural network from features of the target sequence. Finally, we show that our novel methods can be used to assess the confidence of prediction of a conventional threading with an optimized constant weighting factor by considering consensus prediction between them. Implication to the underlined energy landscape of protein folding is discussed.", :month "November", :date_accept nil, :type "2", :created "2013-09-25T17:24:26Z", :howpublished "", :organization nil, :isbn "", :scope nil, :journal "Proteins", :doi "10.1002/prot.22082", :keywords "threading;protein structrure prediction;scoring function;weight optimization;protein folding", :title "Threading Without Optimizing Weighting Factor for the Scoring Function Proteins", :pages "581-96", :volume "73", :note "In revision", :author "Yang, D.Y., Park, Y. and D. Kihara", :chapter nil, :year "2008", :date_submit nil, :research_notes "", :language nil, :label nil, :id 6, :notes nil, :eprint "", :scope_id nil, :exp_list_exp_data nil, :url "http://onlinelibrary.wiley.com/doi/10.1002/prot.22082/full", :editor "", :custom2 nil, :affiliated 1, :edition "", :custom1 nil, :location "", :cite "", :published 1, :custom4 nil, :accession_number "", :booktitle "", :exp_data nil}]
            :citations [{:institution nil, :date_publish nil, :software_use nil, :address nil, :format nil, :fundedby nil, :publisher nil, :key nil, :series nil, :number nil, :school nil, :short_title nil, :custom3 nil, :call_number nil, :uid nil, :formatted nil, :res_edu nil, :author_address nil, :ref_type nil, :params nil, :abstract nil, :month nil, :date_accept nil, :type "Journal", :created nil, :howpublished nil, :organization nil, :isbn nil, :scope nil, :journal nil, :doi nil, :keywords nil, :title "Blah", :pages nil, :volume nil, :note nil, :author nil, :chapter nil, :year "1996", :date_submit nil, :research_notes nil, :language nil, :label nil, :id 401, :notes nil, :eprint nil, :scope_id nil, :exp_list_exp_data nil, :url nil, :editor nil, :custom2 nil, :affiliated 0, :edition nil, :custom1 nil, :location nil, :cite nil, :published 1, :custom4 nil, :accession_number nil, :booktitle nil, :exp_data nil}]
            :create-citation {:institution nil, :date_publish nil, :software_use nil, :address nil, :format nil, :fundedby nil, :publisher nil, :key nil, :series nil, :number nil, :school nil, :short_title nil, :custom3 nil, :call_number nil, :uid nil, :formatted nil, :res_edu nil, :author_address nil, :ref_type nil, :params nil, :abstract nil, :month nil, :date_accept nil, :type "Article", :created nil, :howpublished nil, :organization nil, :isbn nil, :scope nil, :journal nil, :doi nil, :keywords nil, :title nil, :pages nil, :volume nil, :note nil, :author nil, :chapter nil, :year "1996", :date_submit nil, :research_notes nil, :language nil, :label nil, :id 400, :notes nil, :eprint nil, :scope_id nil, :exp_list_exp_data nil, :url nil, :editor nil, :custom2 nil, :affiliated 0, :edition nil, :custom1 nil, :location nil, :cite nil, :published 1, :custom4 nil, :accession_number nil, :booktitle nil, :exp_data nil}
            :rm-citation {:institution nil, :date_publish nil, :software_use nil, :address nil, :format nil, :fundedby nil, :publisher nil, :key nil, :series nil, :number nil, :school nil, :short_title nil, :custom3 nil, :call_number nil, :uid nil, :formatted nil, :res_edu nil, :author_address nil, :ref_type nil, :params nil, :abstract nil, :month nil, :date_accept nil, :type "Article", :created nil, :howpublished nil, :organization nil, :isbn nil, :scope nil, :journal nil, :doi nil, :keywords nil, :title nil, :pages nil, :volume nil, :note nil, :author nil, :chapter nil, :year "1996", :date_submit nil, :research_notes nil, :language nil, :label nil, :id 400, :notes nil, :eprint nil, :scope_id nil, :exp_list_exp_data nil, :url nil, :editor nil, :custom2 nil, :affiliated 0, :edition nil, :custom1 nil, :location nil, :cite nil, :published 1, :custom4 nil, :accession_number nil, :booktitle nil, :exp_data nil}
            :citation-types [{:id 1, :type "journal", :type_title "Journal", :type_desc "", :type_export nil, :fields nil} {:id 2, :type "article", :type_title "Article", :type_desc "", :type_export nil, :fields nil} {:id 3, :type "book", :type_title "Book", :type_desc "", :type_export nil, :fields nil} {:id 4, :type "booklet", :type_title "Booklet", :type_desc "", :type_export nil, :fields nil} {:id 5, :type "conference", :type_title "Conference", :type_desc "", :type_export nil, :fields nil} {:id 6, :type "inbook", :type_title "In Book", :type_desc "", :type_export nil, :fields nil} {:id 7, :type "incollection", :type_title "In Collection", :type_desc "", :type_export nil, :fields nil} {:id 8, :type "inproceedings", :type_title "In Proceedings", :type_desc "", :type_export nil, :fields nil} {:id 9, :type "magazine", :type_title "Magazine", :type_desc "", :type_export nil, :fields nil} {:id 10, :type "manual", :type_title "Manual", :type_desc "", :type_export nil, :fields nil} {:id 11, :type "mastersthesis", :type_title "Masters Thesis", :type_desc "", :type_export nil, :fields nil} {:id 12, :type "misc", :type_title "Misc", :type_desc "", :type_export nil, :fields nil} {:id 13, :type "phdthesis", :type_title "PhD Thesis", :type_desc "", :type_export nil, :fields nil} {:id 14, :type "proceedings", :type_title "Proceedings", :type_desc "", :type_export nil, :fields nil} {:id 15, :type "techreport", :type_title "Tech Report", :type_desc "", :type_export nil, :fields nil} {:id 16, :type "unpublished", :type_title "Unpublished", :type_desc "", :type_export nil, :fields nil} {:id 17, :type "patent", :type_title "Patent", :type_desc "", :type_export nil, :fields nil} {:id 18, :type "chapter", :type_title "Chapter", :type_desc "", :type_export nil, :fields nil} {:id 19, :type "notes", :type_title "Text Snippet/Notes", :type_desc "", :type_export nil, :fields nil} {:id 20, :type "letter", :type_title "Letter", :type_desc "", :type_export nil, :fields nil} {:id 21, :type "xarchive", :type_title "XArchive", :type_desc "", :type_export nil, :fields nil} {:id 22, :type "manuscript", :type_title "Manuscript", :type_desc "", :type_export nil, :fields nil} {:id 23, :type "paper", :type_title "Paper", :type_desc nil, :type_export nil, :fields nil} {:id 24, :type "presentation", :type_title "Presentation", :type_desc nil, :type_export nil, :fields nil} {:id 25, :type "publication", :type_title "Publication", :type_desc nil, :type_export nil, :fields nil} {:id 26, :type "researchreport", :type_title "Research Report", :type_desc nil, :type_export nil, :fields nil} {:id 27, :type "annualreport", :type_title "Annual Report", :type_desc nil, :type_export nil, :fields nil} {:id 28, :type "governmentreport", :type_title "Government report", :type_desc nil, :type_export nil, :fields nil} {:id 29, :type "poster", :type_title "Poster", :type_desc nil, :type_export nil, :fields nil}]
            }
  )

(defn me [db]
  (dispatch [:res/me (:me resps)])
  db
  )

(defn master-types [db]
  (dispatch [:res/master-types (:master-types resps)])
  db
  )

(defn pub [db]
  (dispatch [:res/pub (:pub resps)])
  db
  )

(defn files [db]
  (dispatch [:res/files (:files resps)])
  db
  )

(defn add-file [db file]
  (dispatch [:res/add-file (:add-file resps) file])
  db
  )

(defn rm-file [db k id]
  (dispatch [:res/rm-file (:rm-file resps) k id])
  db
  )

(defn ls-files [db]
  (dispatch [:res/ls-files (:ls-files resps)])
  db
  )

(defn usage [db files]
  (dispatch [:res/usage (:usage resps)])
  db
  )

(defn authors [db]
  (dispatch [:res/authors (:authors resps)])
  db
  )

(defn owners [db]
  (dispatch [:res/owners (:owners resps)])
  db
  )

(defn rm-author [db id]
  (dispatch [:res/rm-author (:rm-author resps) id])
  db
  )

(defn update-author [db]
  (dispatch [:res/update-author (:update-author resps)])
  db
  )

(defn add-author [db]
  (dispatch [:res/add-author (:add-author resps)])
  db
  )

(defn search-users [db]
  (dispatch [:res/search-users (:search-users resps)])
  db
  )

(defn prj [db]
  (dispatch [:res/prj (:prj resps)])
  db
  )

(defn new-author [db]
  (dispatch [:res/new-author (:new-author resps)])
  db
  )

(defn tags [db]
  (dispatch [:res/tags (:tags resps)])
  db
  )

(defn rm-tag [db]
  (dispatch [:res/rm-tag (:rm-tag resps)])
  db
  )

(defn add-tag [db]
  (dispatch [:res/add-tag (:add-tag resps)])
  db
  )

(defn search-tags [db]
  (dispatch [:res/search-tags (:search-tags resps)])
  db
  )

(defn licenses [db]
  (dispatch [:res/licenses (:licenses resps)])
  db
  )

(defn license [db]
  (dispatch [:res/license (:license resps)])
  db
  )

(defn save-pub [db]
  (dispatch [:res/save-pub (:save-pub resps)])
  db
  )

(defn citations [db]
  (dispatch [:res/citations (:citations resps)])
  db
  )

(defn add-citation [db]
  (dispatch [:res/add-citation (:add-citation resps)])
  db
  )

(defn search-citations [db]
  (dispatch [:res/search-citations (:search-citations resps)])
  db
  )

(defn create-citation [db]
  (dispatch [:res/create-citation (:create-citation resps)])
  db
  )

(defn rm-citation [db]
  (dispatch [:res/rm-citation (:rm-citation resps)])
  db
  )

(defn citation-types [db]
  (dispatch [:res/citation-types (:citation-types resps)])
  db
  )

