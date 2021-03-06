package org.jsoup.parser;

import com.mp3download.zingmp3.C1569R;
import java.util.Iterator;
import java.util.List;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;

public class XmlTreeBuilder extends TreeBuilder {

    /* renamed from: org.jsoup.parser.XmlTreeBuilder.1 */
    static /* synthetic */ class C15961 {
        static final /* synthetic */ int[] $SwitchMap$org$jsoup$parser$Token$TokenType;

        static {
            $SwitchMap$org$jsoup$parser$Token$TokenType = new int[TokenType.values().length];
            try {
                $SwitchMap$org$jsoup$parser$Token$TokenType[TokenType.StartTag.ordinal()] = 1;
            } catch (NoSuchFieldError e) {
            }
            try {
                $SwitchMap$org$jsoup$parser$Token$TokenType[TokenType.EndTag.ordinal()] = 2;
            } catch (NoSuchFieldError e2) {
            }
            try {
                $SwitchMap$org$jsoup$parser$Token$TokenType[TokenType.Comment.ordinal()] = 3;
            } catch (NoSuchFieldError e3) {
            }
            try {
                $SwitchMap$org$jsoup$parser$Token$TokenType[TokenType.Character.ordinal()] = 4;
            } catch (NoSuchFieldError e4) {
            }
            try {
                $SwitchMap$org$jsoup$parser$Token$TokenType[TokenType.Doctype.ordinal()] = 5;
            } catch (NoSuchFieldError e5) {
            }
            try {
                $SwitchMap$org$jsoup$parser$Token$TokenType[TokenType.EOF.ordinal()] = 6;
            } catch (NoSuchFieldError e6) {
            }
        }
    }

    protected void initialiseParse(String input, String baseUri, ParseErrorList errors) {
        super.initialiseParse(input, baseUri, errors);
        this.stack.add(this.doc);
    }

    protected boolean process(Token token) {
        switch (C15961.$SwitchMap$org$jsoup$parser$Token$TokenType[token.type.ordinal()]) {
            case C1569R.styleable.com_facebook_profile_picture_view_com_facebook_is_cropped /*1*/:
                insert(token.asStartTag());
                break;
            case C1569R.styleable.com_facebook_login_view_com_facebook_logout_text /*2*/:
                popStackToClose(token.asEndTag());
                break;
            case C1569R.styleable.com_facebook_login_view_com_facebook_tooltip_mode /*3*/:
                insert(token.asComment());
                break;
            case C1569R.styleable.com_facebook_like_view_com_facebook_auxiliary_view_position /*4*/:
                insert(token.asCharacter());
                break;
            case C1569R.styleable.com_facebook_like_view_com_facebook_horizontal_alignment /*5*/:
                insert(token.asDoctype());
                break;
            case C1569R.styleable.Toolbar_contentInsetEnd /*6*/:
                break;
            default:
                Validate.fail("Unexpected token type: " + token.type);
                break;
        }
        return true;
    }

    private void insertNode(Node node) {
        currentElement().appendChild(node);
    }

    Element insert(StartTag startTag) {
        Tag tag = Tag.valueOf(startTag.name());
        Element el = new Element(tag, this.baseUri, startTag.attributes);
        insertNode(el);
        if (startTag.isSelfClosing()) {
            this.tokeniser.acknowledgeSelfClosingFlag();
            if (!tag.isKnownTag()) {
                tag.setSelfClosing();
            }
        } else {
            this.stack.add(el);
        }
        return el;
    }

    void insert(Comment commentToken) {
        Node comment = new Comment(commentToken.getData(), this.baseUri);
        Node insert = comment;
        if (commentToken.bogus) {
            String data = comment.getData();
            if (data.length() > 1 && (data.startsWith("!") || data.startsWith("?"))) {
                insert = new XmlDeclaration(data.substring(1), comment.baseUri(), data.startsWith("!"));
            }
        }
        insertNode(insert);
    }

    void insert(Character characterToken) {
        insertNode(new TextNode(characterToken.getData(), this.baseUri));
    }

    void insert(Doctype d) {
        insertNode(new DocumentType(d.getName(), d.getPublicIdentifier(), d.getSystemIdentifier(), this.baseUri));
    }

    private void popStackToClose(EndTag endTag) {
        String elName = endTag.name();
        Element firstFound = null;
        Iterator<Element> it = this.stack.descendingIterator();
        while (it.hasNext()) {
            Element next = (Element) it.next();
            if (next.nodeName().equals(elName)) {
                firstFound = next;
                break;
            }
        }
        if (firstFound != null) {
            it = this.stack.descendingIterator();
            while (it.hasNext()) {
                if (((Element) it.next()) == firstFound) {
                    it.remove();
                    return;
                }
                it.remove();
            }
        }
    }

    List<Node> parseFragment(String inputFragment, String baseUri, ParseErrorList errors) {
        initialiseParse(inputFragment, baseUri, errors);
        runParser();
        return this.doc.childNodes();
    }
}
