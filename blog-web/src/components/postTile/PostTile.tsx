import React from 'react';
import { FaEdit, FaTrash, FaUndo, FaExternalLinkAlt } from 'react-icons/fa';
import Post from '@/models/post';
import { Button } from '@/components/ui/button';

interface PostTileProps {
    post: Post;
    onEdit?: (id: string) => void;
    onDelete?: (id: string) => void;
    onRestore?: (id: string) => void;
    onOpen?: (id: string) => void;
}

const PostTile: React.FC<PostTileProps> = ({ post, onEdit, onDelete, onRestore, onOpen }) => {
    return (
        <div className="bg-card rounded-xl shadow-sm border border-border px-5 py-3 flex flex-row items-center justify-between w-full">
            <div className="flex flex-col gap-1 min-w-0">
                <h3 className="text-base font-semibold text-foreground truncate">{post.title}</h3>
                <p className="text-xs text-muted-foreground">{post.creationDate}</p>
                <p className="text-xs text-muted-foreground truncate">{post.slug}</p>
            </div>
            <div className="flex gap-2 flex-shrink-0 ml-4">
                {onEdit && (
                    <Button variant="outline" size="icon" onClick={() => onEdit(post.postId)} aria-label="Edit">
                        <FaEdit className="w-4 h-4" />
                    </Button>
                )}
                {onDelete && (
                    <Button variant="outline" size="icon" onClick={() => onDelete(post.postId)} aria-label="Delete">
                        <FaTrash className="w-4 h-4" />
                    </Button>
                )}
                {onRestore && (
                    <Button variant="outline" size="icon" onClick={() => onRestore(post.postId)} aria-label="Restore">
                        <FaUndo className="w-4 h-4" />
                    </Button>
                )}
                {onOpen && (
                    <Button variant="outline" size="icon" onClick={() => onOpen(post.postId)} aria-label="Open">
                        <FaExternalLinkAlt className="w-4 h-4" />
                    </Button>
                )}
            </div>
        </div>
    );
};

export default PostTile;