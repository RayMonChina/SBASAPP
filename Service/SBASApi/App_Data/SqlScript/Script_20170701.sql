/****** Object:  Table [dbo].[MeterEquipment]    Script Date: 2017/7/1 17:49:03 ******/
SET ANSI_NULLS ON
GO

SET QUOTED_IDENTIFIER ON
GO

CREATE TABLE [dbo].[MeterEquipment](
	[MEID] [int] IDENTITY(1,1) NOT NULL,
	[MECode] [nvarchar](50) NOT NULL,
	[LoginID] [nvarchar](50) NULL,
	[RegTime] [datetime] NOT NULL,
	[States] [int] NOT NULL,
 CONSTRAINT [PK_MeterEquipment_1] PRIMARY KEY CLUSTERED 
(
	[MEID] ASC,
	[MECode] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON) ON [PRIMARY]
) ON [PRIMARY]

GO

ALTER TABLE [dbo].[MeterEquipment] ADD  CONSTRAINT [DF_MeterEquipment_RegTime]  DEFAULT (getdate()) FOR [RegTime]
GO

ALTER TABLE [dbo].[MeterEquipment] ADD  CONSTRAINT [DF_MeterEquipment_States]  DEFAULT ((0)) FOR [States]
GO

EXEC sys.sp_addextendedproperty @name=N'MS_Description', @value=N'0-Î´×¢²á£»1-ÒÑ×¢²á£»2-Ëø¶¨' , @level0type=N'SCHEMA',@level0name=N'dbo', @level1type=N'TABLE',@level1name=N'MeterEquipment', @level2type=N'COLUMN',@level2name=N'States'
GO