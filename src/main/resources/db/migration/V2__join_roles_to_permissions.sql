create table roles_to_permissions (
    role_id bigint not null,
    permission_id bigint not null,
    primary key (role_id, permission_id),
    constraint fk_roles_to_permissions_role foreign key (role_id) references roles(id) on delete cascade,
    constraint fk_roles_to_permissions_permission foreign key (permission_id) references permissions(id) on delete cascade
);

INSERT INTO roles_to_permissions (role_id, permission_id)
SELECT roles.id, permissions.id
FROM (VALUES
      ('guest', 'view_gallery'),
      ('registered-viewer', 'view_full_image'),
      ('registered-viewer', 'view_gallery'),
      ('registered-viewer', 'view_registered_user'),
      ('registered-viewer', 'use_image_like'),
      ('registered-viewer', 'use_follow'),
      ('registered-viewer', 'use_image_report'),
      ('registered-artist', 'view_full_image'),
      ('registered-artist', 'view_gallery'),
      ('registered-artist', 'view_registered_user'),
      ('registered-artist', 'use_image_upload'),
      ('registered-artist', 'use_image_like'),
      ('registered-artist', 'use_follow'),
      ('registered-artist', 'use_image_report'),
      ('administrator', 'view_full_image'),
      ('administrator', 'view_gallery'),
      ('administrator', 'view_registered_user'),
      ('administrator', 'use_image_download'),
      ('administrator', 'use_image_report'),
      ('administrator', 'use_image_report_comments'),
      ('administrator', 'use_image_report_close'),
      ('administrator', 'use_image_delete')
 ) AS data(r, p)
 JOIN roles ON roles.name = data.r
 JOIN permissions ON permissions.name = data.p;