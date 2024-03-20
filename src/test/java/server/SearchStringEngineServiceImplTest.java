package server;

import com.axreng.backend.model.response.SearchRequestResponseDTO;
import com.axreng.backend.server.SearchResultManager;
import com.axreng.backend.server.impl.SearchResultManagerImpl;
import com.axreng.backend.server.impl.SearchStringEngineServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import static com.axreng.backend.server.impl.SearchResultManagerImpl.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.when;

public class SearchStringEngineServiceImplTest {
    @Mock
    SearchResultManagerImpl searchResultManager = new SearchResultManagerImpl();
    SearchStringEngineServiceImpl searchStringEngineService = new SearchStringEngineServiceImpl();

    @BeforeEach
    void setUp() {

    }

    @Test
    void testKMPSearch() {

        String pattern = "abc";
        String text = "ababcabcabababd";
        String link = "link";
        int[] lps = searchStringEngineService.computeLPSArray(pattern);
        String id = searchResultManager.createInitialSearchProcess();

        searchResultManager.addLinkById(id, link);
        searchStringEngineService.searchAnyMatch(text, pattern, id, link, lps);
        SearchRequestResponseDTO updatedResult = searchResultManager.getResult(id);
        assertNotNull(updatedResult);
        assertTrue(updatedResult.getUrls().contains(link));

    }

    @Test
    void testKMPSearchWithHTML() {

        String pattern = "Documentation";
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                "XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; " +
                "charset=UTF-8\" /><title>Appendix A. GNU Free Documentation License</title><link rel=\"stylesheet\" " +
                "type=\"text/css\" href=\"stylesheet/manpages.css\" /><meta name=\"generator\" content=\"DocBook " +
                "XSL Stylesheets Vsnapshot\" /><link rel=\"home\" href=\"index.html\" title=\"Linux manual pages\" />" +
                "<link rel=\"up\" href=\"index.html\" title=\"Linux manual pages\" /><link rel=\"prev\" " +
                "href=\"index8.html\" title=\"Section 8\" /><link rel=\"next\" href=\"gfdl-1.html\" " +
                "title=\"APPLICABILITY AND DEFINITIONS\" /><script type=\"text/javascript\" src=\"stylesheet/manpages.js\">" +
                "</script><link rel=\"icon\" href=\"stylesheet/icon.gif\" type=\"image/gif\" /></head><body " +
                "onload=\"javascript:init()\"><div class=\"navheader\"><table width=\"100%\" summary=\"Navigation header\">" +
                "<tr><th colspan=\"3\" align=\"center\">Appendix A. GNU Free Documentation License</th></tr><tr><td width=\"20%\" align=\"left\"><a accesskey=\"p\" href=\"index8.html\">Prev</a> </td><th width=\"60%\" align=\"center\"> </th><td width=\"20%\" align=\"right\"> <a accesskey=\"n\" href=\"gfdl-1.html\">Next</a></td></tr></table><hr /></div><div class=\"appendix\"><div class=\"titlepage\"><div><div><h2 class=\"title\"><a id=\"gfdl\"></a>Appendix A. GNU Free Documentation License</h2></div><div><p class=\"releaseinfo\">Version 1.2, November 2002</p></div><div><p class=\"copyright\">Copyright © 2000,2001,2002 Free Software Foundation, Inc.</p></div><div><div class=\"legalnotice\"><a id=\"gfdl-legalnotice\"></a><div class=\"address\"><p>Free Software Foundation, Inc.<br /> <span class=\"street\">51 Franklin St, Fifth Floor</span>,<br /> <span class=\"city\">Boston</span>,<br /> <span class=\"state\">MA</span><br /> <span class=\"postcode\">02110-1301</span><br /> <span class=\"country\">USA</span><br /> </p></div><p>Everyone is permitted to copy and distribute verbatim copies of this license document, but changing it is not allowed.</p></div></div><div><p class=\"pubdate\">Version 1.2, November 2002</p></div></div></div><div class=\"section\"><div class=\"titlepage\"><div><div><h2 class=\"title\" style=\"clear: both\"><a id=\"gfdl-0\"></a>PREAMBLE</h2></div></div></div><p>The purpose of this License is to make a manual, textbook, orother functional and useful document \"free\" in the sense of freedom: toassure everyone the effective freedom to copy and redistribute it, withor without modifying it, either commercially or noncommercially.Secondarily, this License preserves for the author and publisher a wayto get credit for their work, while not being considered responsible formodifications made by others.</p><p>This License is a kind of \"copyleft\", which means that derivativeworks of the document must themselves be free in the same sense.  Itcomplements the GNU General Public License, which is a copyleft licensedesigned for free software.</p><p>We have designed this License in order to use it for manuals forfree software, because free software needs free documentation: a freeprogram should come with manuals providing the same freedoms that thesoftware does.  But this License is not limited to software manuals; itcan be used for any textual work, regardless of subject matter orwhether it is published as a printed book.  We recommend this Licenseprincipally for works whose purpose is instruction or reference.</p></div></div><div class=\"navfooter\"><hr /><table width=\"100%\" summary=\"Navigation footer\"><tr><td width=\"40%\" align=\"left\"><a accesskey=\"p\" href=\"index8.html\">Prev</a> </td><td width=\"20%\" align=\"center\"> </td><td width=\"40%\" align=\"right\"> <a accesskey=\"n\" href=\"gfdl-1.html\">Next</a></td></tr><tr><td width=\"40%\" align=\"left\" valign=\"top\">Section 8 </td><td width=\"20%\" align=\"center\"><a accesskey=\"h\" href=\"index.html\">Home</a></td><td width=\"40%\" align=\"right\" valign=\"top\"> APPLICABILITY AND DEFINITIONS</td></tr></table></div></body></html>";
        String link = "link";
        var id = searchResultManager.createInitialSearchProcess();
        int[] lps = searchStringEngineService.computeLPSArray(pattern);

        searchResultManager.addLinkById(id, link);
        searchStringEngineService.searchAnyMatch(text, pattern, id, link, lps);
        SearchRequestResponseDTO updatedResult = searchResultManager.getResult(id);
        assertNotNull(updatedResult);


    }

    @Test
    void testKMPSearchIscaseSensitive() {

        String pattern = "documentation";
        String text = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><!DOCTYPE html PUBLIC \"-//W3C//DTD " +
                "XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">" +
                "<html xmlns=\"http://www.w3.org/1999/xhtml\"><head><meta http-equiv=\"Content-Type\" content=\"text/html; " +
                "charset=UTF-8\" /><title>Appendix A. GNU Free Documentation License</title><link rel=\"stylesheet\" " +
                "type=\"text/css\" href=\"stylesheet/manpages.css\" /><meta name=\"generator\" content=\"DocBook " +
                "XSL Stylesheets Vsnapshot\" /><link rel=\"home\" href=\"index.html\" title=\"Linux manual pages\" />" +
                "<link rel=\"up\" href=\"index.html\" title=\"Linux manual pages\" /><link rel=\"prev\" " +
                "href=\"index8.html\" title=\"Section 8\" /><link rel=\"next\" href=\"gfdl-1.html\" " +
                "title=\"APPLICABILITY AND DEFINITIONS\" /><script type=\"text/javascript\" src=\"stylesheet/manpages.js\">" +
                "</script><link rel=\"icon\" href=\"stylesheet/icon.gif\" type=\"image/gif\" /></head><body " +
                "onload=\"javascript:init()\"><div class=\"navheader\"><table width=\"100%\" summary=\"Navigation header\">" +
                "<tr><th colspan=\"3\" align=\"center\">Appendix A. GNU Free Documentation License</th></tr><tr><td width=\"20%\" align=\"left\"><a accesskey=\"p\" href=\"index8.html\">Prev</a> </td><th width=\"60%\" align=\"center\"> </th><td width=\"20%\" align=\"right\"> <a accesskey=\"n\" href=\"gfdl-1.html\">Next</a></td></tr></table><hr /></div><div class=\"appendix\"><div class=\"titlepage\"><div><div><h2 class=\"title\"><a id=\"gfdl\"></a>Appendix A. GNU Free Documentation License</h2></div><div><p class=\"releaseinfo\">Version 1.2, November 2002</p></div><div><p class=\"copyright\">Copyright © 2000,2001,2002 Free Software Foundation, Inc.</p></div><div><div class=\"legalnotice\"><a id=\"gfdl-legalnotice\"></a><div class=\"address\"><p>Free Software Foundation, Inc.<br /> <span class=\"street\">51 Franklin St, Fifth Floor</span>,<br /> <span class=\"city\">Boston</span>,<br /> <span class=\"state\">MA</span><br /> <span class=\"postcode\">02110-1301</span><br /> <span class=\"country\">USA</span><br /> </p></div><p>Everyone is permitted to copy and distribute verbatim copies of this license document, but changing it is not allowed.</p></div></div><div><p class=\"pubdate\">Version 1.2, November 2002</p></div></div></div><div class=\"section\"><div class=\"titlepage\"><div><div><h2 class=\"title\" style=\"clear: both\"><a id=\"gfdl-0\"></a>PREAMBLE</h2></div></div></div><p>The purpose of this License is to make a manual, textbook, orother functional and useful document \"free\" in the sense of freedom: toassure everyone the effective freedom to copy and redistribute it, withor without modifying it, either commercially or noncommercially.Secondarily, this License preserves for the author and publisher a wayto get credit for their work, while not being considered responsible formodifications made by others.</p><p>This License is a kind of \"copyleft\", which means that derivativeworks of the document must themselves be free in the same sense.  Itcomplements the GNU General Public License, which is a copyleft licensedesigned for free software.</p><p>We have designed this License in order to use it for manuals forfree software, because free software needs free documentation: a freeprogram should come with manuals providing the same freedoms that thesoftware does.  But this License is not limited to software manuals; itcan be used for any textual work, regardless of subject matter orwhether it is published as a printed book.  We recommend this Licenseprincipally for works whose purpose is instruction or reference.</p></div></div><div class=\"navfooter\"><hr /><table width=\"100%\" summary=\"Navigation footer\"><tr><td width=\"40%\" align=\"left\"><a accesskey=\"p\" href=\"index8.html\">Prev</a> </td><td width=\"20%\" align=\"center\"> </td><td width=\"40%\" align=\"right\"> <a accesskey=\"n\" href=\"gfdl-1.html\">Next</a></td></tr><tr><td width=\"40%\" align=\"left\" valign=\"top\">Section 8 </td><td width=\"20%\" align=\"center\"><a accesskey=\"h\" href=\"index.html\">Home</a></td><td width=\"40%\" align=\"right\" valign=\"top\"> APPLICABILITY AND DEFINITIONS</td></tr></table></div></body></html>";
        String link = "link";
        var id = searchResultManager.createInitialSearchProcess();
        int[] lps = searchStringEngineService.computeLPSArray(pattern);

        searchStringEngineService.searchAnyMatch(text, pattern, id, link, lps);
        SearchRequestResponseDTO updatedResult = searchResultManager.getResult(id);
        assertNotNull(updatedResult);


    }
}
