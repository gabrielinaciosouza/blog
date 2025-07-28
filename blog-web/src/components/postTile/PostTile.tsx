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
        <div className="bg-card rounded-xl shadow-sm border border-border px-4 py-3 w-full flex flex-col sm:flex-row sm:items-center sm:justify-between gap-2">
            <div className="flex flex-col min-w-0 flex-1">
                <h3 className="text-base font-semibold text-foreground break-words whitespace-normal">{post.title}</h3>
                <div className="flex gap-2 text-xs text-muted-foreground">
                    <span>{post.creationDate}</span>
                    <span className="truncate max-w-[120px] sm:max-w-[200px]">{post.slug}</span>
                </div>
            </div>
            <div className="flex gap-2 mt-2 sm:mt-0 sm:ml-4 w-full sm:w-auto">
                {onEdit && (
                    <Button variant="outline" size="icon" className="w-full sm:w-auto sm:px-4 sm:py-3" onClick={() => onEdit(post.postId)} aria-label="Edit">
                        <FaEdit className="w-4 h-4" />
                    </Button>
                )}
                {onDelete && (
                    <Button variant="outline" size="icon" className="w-full sm:w-auto sm:px-4 sm:py-3" onClick={() => onDelete(post.postId)} aria-label="Delete">
                        <FaTrash className="w-4 h-4" />
                    </Button>
                )}
                {onRestore && (
                    <Button variant="outline" size="icon" className="w-full sm:w-auto sm:px-4 sm:py-3" onClick={() => onRestore(post.postId)} aria-label="Restore">
                        <FaUndo className="w-4 h-4" />
                    </Button>
                )}
                {onOpen && (
                    <Button variant="outline" size="icon" className="w-full sm:w-auto sm:px-4 sm:py-3" onClick={() => onOpen(post.postId)} aria-label="Open">
                        <FaExternalLinkAlt className="w-4 h-4" />
                    </Button>
                )}
            </div>
        </div>
    );
};

export default PostTile;