module de.be.thaw.reference {
    requires org.jetbrains.annotations;
    requires com.fasterxml.jackson.databind;
    requires de.undercouch.citeproc;
    requires org.jbibtex;
    requires de.be.thaw.shared;
    requires de.be.thaw.info;
    requires de.be.thaw.util;
    requires java.logging;
    requires org.jsoup;

    exports de.be.thaw.reference;
    exports de.be.thaw.reference.impl;
    exports de.be.thaw.reference.citation;
    exports de.be.thaw.reference.citation.exception;
    exports de.be.thaw.reference.citation.csl;
    exports de.be.thaw.reference.citation.empty;
    exports de.be.thaw.reference.citation.referencelist;

}
