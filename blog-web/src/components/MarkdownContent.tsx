import React from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import rehypeRaw from "rehype-raw";

interface MarkdownContentProps {
    content: string;
}

const MarkdownContent: React.FC<MarkdownContentProps> = ({ content }) => (

    <div className="prose prose-neutral max-w-none">
        <ReactMarkdown
            remarkPlugins={[remarkGfm]}
            rehypePlugins={[rehypeRaw]}
            components={{
                a: ({ href = '', children }) => (
                    <a
                        href={href.startsWith('http') ? href : `https://${href}`}
                        target="_blank"
                        rel="noopener noreferrer"
                        className="text-blue-600 underline"
                    >
                        {children}
                    </a>
                ),
                img: ({ src = '', alt = '' }) => (
                    <img
                        src={src}
                        alt={alt}
                        style={{
                            width: '100%',
                            maxWidth: '500px', // define a largura exata visual
                            height: 'auto',
                            display: 'block',
                            margin: '1.5rem auto',
                            objectFit: 'cover',
                        }}
                    />
                ),
                h2: ({ node, ...props }) => <h2 className="text-2xl font-bold mt-6 mb-2" {...props} />,
                h3: ({ node, ...props }) => <h3 className="text-xl font-semibold mt-4 mb-2" {...props} />,
                ol: ({ node, ...props }) => <ol className="list-decimal list-inside" {...props} />,
                ul: ({ node, ...props }) => <ul className="list-disc list-inside" {...props} />,
                br: () => <br />,
                p: ({ node, ...props }) => <p className="mb-4" {...props} />,
            }}
        >
            {content}
        </ReactMarkdown>

    </div>
);

export default MarkdownContent;
