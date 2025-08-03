import React from "react";
import ReactMarkdown from "react-markdown";
import remarkGfm from "remark-gfm";
import rehypeRaw from "rehype-raw";
import Image from "next/image";

interface MarkdownContentProps {
    content: string;
    imageWidth?: number;
    imageHeight?: number;
}

const MarkdownContent: React.FC<MarkdownContentProps> = ({ content, imageWidth = 500, imageHeight = 281 }) => (

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
                img: ({ src = '', alt = '', width, height }) => (
                    <Image
                        src={src}
                        alt={alt}
                        width={width ? Number(width) : imageWidth}
                        height={height ? Number(height) : imageHeight}
                        style={{
                            width: '100%',
                            maxWidth: imageWidth,
                            display: 'block',
                            margin: '1.5rem auto',
                            objectFit: 'cover',
                        }}
                    />
                ),
                h2: (props) => <h2 className="text-2xl font-bold mt-6 mb-2" {...props} />,
                h3: (props) => <h3 className="text-xl font-semibold mt-4 mb-2" {...props} />,
                ol: (props) => <ol className="list-decimal list-inside" {...props} />,
                ul: (props) => <ul className="list-disc list-inside" {...props} />,
                br: () => <br />,
                p: (props) => <p className="mb-4" {...props} />,
            }}
        >
            {content}
        </ReactMarkdown>

    </div>
);

export default MarkdownContent;
