USE [Db]
GO
/****** Object:  Table [dbo].[DummyLeaks]    Script Date: 5/31/2021 5:52:02 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[DummyLeaks](
	[Id] [uniqueidentifier] NOT NULL,
	[Site] [nvarchar](max) NULL,
	[Url] [nvarchar](max) NULL,
	[Email] [nvarchar](max) NULL,
 CONSTRAINT [PK_DummyLeaks] PRIMARY KEY CLUSTERED 
(
	[Id] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY] TEXTIMAGE_ON [PRIMARY]
GO
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'6a5b5f5d-cde1-43e3-9a62-08d92473b7ea', N'Amazon', N'https://www.amazon.com', N'admin@admin.com')
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'49447d69-8ed6-4cd4-9a63-08d92473b7ea', N'Aulas', N'https://www.aulas.com', N'diego@admin.com')
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'0f34edec-7035-4819-9a64-08d92473b7ea', N'Aulas', N'https://www.aulas.com', N'fede@admin.com')
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'e566c961-5a2e-42ba-9a65-08d92473b7ea', N'Aulas', N'https://www.aulas.com', N'juan@admin.com')
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'5e7940ea-c8f1-470d-9a66-08d92473b7ea', N'adminBook', N'https://www.adminBook.com', N'admin2@admin.com')
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'16004ad8-adf2-4e75-9a67-08d92473b7ea', N'adminGram', N'https://www.admingram.com', N'admin2@admin.com')
INSERT [dbo].[DummyLeaks] ([Id], [Site], [Url], [Email]) VALUES (N'576647e2-4b37-4b76-bb1d-c548f212f981', N'Amazon', N'https://www.amazon.com', N'admin@admin.com')
